package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.DbUtil;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonApiImpl implements PersonApi {
    @Value("${eventsourcing.queryqueue}")
    private String queryQueue;
    private DataSource dataSource;
    private ConnectionFactory connectionFactory;

    @Autowired
    public PersonApiImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        try {
            this.dataSource = DbUtil.buildDataSource();
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных");
        }
    }

    public String getQueryQueue() {
        return queryQueue;
    }

    @Override
    public void deletePerson(Long personId) {
        // Создаем объект класса Person
        Person person = new Person(personId, null, null, null);
        String message = convertToJson(person);

        // Создаем сообщение-команду на удаление персоны
        sendMessageToQueue("DELETE" + message);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        // Создаем объект класса Person
        Person person = new Person(personId, firstName, lastName, middleName);
        String message = convertToJson(person);

        // Создаем сообщение-команду на сохранение персоны
        sendMessageToQueue("SAVE" + message);
    }

    @Override
    public Person findPerson(Long personId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM person WHERE person_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, personId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new Person(resultSet.getLong("person_id"), resultSet.getString("first_name"),
                                resultSet.getString("last_name"), resultSet.getString("middle_name"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(String.format("Не найден объект Person с ID = %d", personId));
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("person_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String middleName = rs.getString("middle_name");
                persons.add(new Person(id, firstName, lastName, middleName));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса findAll()");
        }
        return persons;
    }

    public void sendMessageToQueue(String message) {
        try {
            // Создаем соединение с RabbitMQ
            com.rabbitmq.client.Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            //Публикуем сообщение-команду в очередь команд
            channel.basicPublish("", queryQueue, null, message.getBytes());

            System.out.println("Отправлено сообщение: " + message);

            // Закрываем соединение с RabbitMQ
            channel.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при отправке сообщения в брокер сообщений");;
        }
    }

    private String convertToJson(Person person) {
        // Преобразуем объект в JSON-строку
        ObjectMapper mapper = new ObjectMapper();
        String message;
        try {
            message = mapper.writeValueAsString(person);
            return message;
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка при сериализации объекта в JSON");
            return null;
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
