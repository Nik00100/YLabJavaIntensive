package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Component
public class Censor {
    @Value("${messagefilter.tablename}")
    private String tableName;
    @Value("${messagefilter.inputqueue}")
    private String inputQueue;
    @Value("${messagefilter.outputqueue}")
    private String outputQueue;
    @Value("${messagefilter.quitcommand}")
    private String quitCommand;

    private DataSource dataSource;
    private ConnectionFactory connectionFactory;

    @Autowired
    public Censor(DataSource dataSource, ConnectionFactory connectionFactory) {
        this.dataSource = dataSource;
        this.connectionFactory = connectionFactory;
    }

    private boolean wordExistsInDatabase(String word) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE word = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet result = statement.executeQuery()) {
                return result.next(); // true, если слово найдено, false в противном случае
            }
        }
    }

    private String replaceCharacter (String word) {
        StringBuilder sb = new StringBuilder();
        sb.append(word.charAt(0));
        for (int i = 1; i < word.length() - 1; i++) {
            sb.append("*");
        }
        sb.append(word.charAt(word.length() - 1));
        return sb.toString();
    }

    private String getFormattedSentence (String sentence) throws SQLException {
        StringBuilder sb = new StringBuilder();
        StringBuilder currentSb = new StringBuilder();

        int i = 0;
        while (i < sentence.length() && !Character.isDigit(sentence.charAt(i)) && !Character.isLetter(sentence.charAt(i))) {
            sb.append(sentence.charAt(i));
            i++; // продвигаем курсор, пока он не укажет на буквенный или цифровой символ
        }

        while (i < sentence.length()) {
            char ch = sentence.charAt(i);
            if (Character.isLetter(ch) || Character.isDigit(ch)) { // если символ буква или цифра - добавляем в текущий StringBuilder
                currentSb.append(ch);
            } else {
                String word = currentSb.toString();
                if (wordExistsInDatabase(word.toLowerCase())) { // проверка на наличие слова в базе данных
                    word = replaceCharacter(word);
                }
                sb.append(word);
                sb.append(ch);
                currentSb.delete(0, currentSb.length()); // сбрасываем текущий StringBuilder
            }
            i++;
        }

        // если в текущем StringBuilder осталось слово
        if (currentSb.length() != 0) {
            if (wordExistsInDatabase(currentSb.toString().toLowerCase())) {
                sb.append(replaceCharacter(currentSb.toString()));
            } else {
                sb.append(currentSb.toString());
            }
        }

        return sb.toString();
    }

    public void rabbitListener() {
        try (com.rabbitmq.client.Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(inputQueue, true, false, false, null); // Назначаем очередь input
            channel.queueDeclare(outputQueue, true, false, false, null); // Назначаем очередь output

            while (true) {
                Thread.sleep(7000); // засыпаем на 7 секунд и продолжаем цикл
                GetResponse response = channel.basicGet(inputQueue, false); // получаем сообщение
                if (response != null) {
                    String message = new String(response.getBody(), StandardCharsets.UTF_8);
                    System.out.println("Изначальное сообщение, пришедшее из очереди RabbitMQ: " + message);

                    if (message.equals(quitCommand)) {
                        channel.basicAck(response.getEnvelope().getDeliveryTag(), false); // подтверждаем обработку сообщения
                        break; // выходим из приложения, если пришла данная команда
                    }

                    System.out.println("Отформатированное сообщение, исходя из соответствия таблице нецензурных слов: "
                            + getFormattedSentence(message));
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false); // подтверждаем обработку сообщения

                    //Публикуем сообщение-ответ в очередь output команд
                    channel.basicPublish("", outputQueue, null, getFormattedSentence(message).getBytes());
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        } catch (IOException | TimeoutException e) {
            System.out.println("Ошибка при обращении к брокеру сообщений");
            e.printStackTrace();
        } catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Ошибка при работе данного потока");
        }
    }

}
