package io.ylab.intensive.lesson04.eventsourcing.db;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

public class DbApp {
    private static final String QUARY_QUEUE = "Query_queue";

    public static void main(String[] args) throws Exception {
        DataSource dataSource = initDb();
        ConnectionFactory connectionFactory = initMQ();

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            while (true) {
                GetResponse response = channel.basicGet(QUARY_QUEUE, false); // получаем сообщение
                if (response != null) {
                    Thread.sleep(7000); // засыпаем на 7 секунд и продолжаем цикл
                    String message = new String(response.getBody(), StandardCharsets.UTF_8);
                    processMessage(message, dataSource);
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false); // подтверждаем обработку сообщения
                }
            }
        }

    }


    private static void processMessage(String message, DataSource dataSource) {
        System.out.println(message);

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

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        String ddl = ""
                + "drop table if exists person cascade;\n"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ");";

        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }
}
