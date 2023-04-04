package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbApp {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();

        // тут пишем создание и запуск приложения работы с БД
        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        String queryQueue = applicationContext.getBean("getQueryQueue", String.class);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queryQueue, true, false, false, null); // Назначаем очередь

            while (true) {
                Thread.sleep(7000); // засыпаем на 7 секунд и продолжаем цикл
                GetResponse response = channel.basicGet(queryQueue, false); // получаем сообщение
                if (response != null) {
                    String message = new String(response.getBody(), StandardCharsets.UTF_8);
                    System.out.println(message);

                    if (message.equals("QUIT")) {
                        channel.basicAck(response.getEnvelope().getDeliveryTag(), false); // подтверждаем обработку сообщения
                        break; // выходим из приложения, если пришла данная команда
                    }

                    processMessage(message, dataSource);
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false); // подтверждаем обработку сообщения
                }
            }
        }
    }

    private static void processMessage(String message, DataSource dataSource) {
        try (java.sql.Connection connection = dataSource.getConnection()) {
            if (message.startsWith("SAVE")) {
                // Парсим данные из сообщения
                String json = message.replaceAll("SAVE", "");
                Person person = fromJson(json);
                long personId = person.getId();
                String firstName = person.getName();
                String lastName = person.getLastName();
                String middleName = person.getMiddleName();

                // Выполняем запрос на вставку данных
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO person " +
                        "(person_id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)")) {
                    statement.setLong(1, personId);
                    statement.setString(2, firstName);
                    statement.setString(3, lastName);
                    statement.setString(4, middleName);
                    statement.executeUpdate();
                }
            } else if (message.startsWith("DELETE")) {
                // Парсим данные из сообщения
                String json = message.replaceAll("DELETE", "");
                Person person = fromJson(json);
                long personId = person.getId();

                // Выполняем запрос на удаление данных
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE person_id = ?")) {
                    statement.setLong(1, personId);
                    statement.executeUpdate();
                }
            } else {
                // Если сообщение не соответствует формату SAVE или DELETE, выбрасываем исключение
                System.out.println("Неверный формат сообщения");
            }
        } catch (SQLException ex) {
            // Обрабатываем исключение при работе с БД
            ex.printStackTrace();
        }
    }

    private static Person fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Person.class);
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка при десериализации объекта из JSON");
            return null;
        }
    }
}
