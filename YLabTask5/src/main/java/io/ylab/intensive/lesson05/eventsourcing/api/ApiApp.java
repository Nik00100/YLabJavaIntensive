package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class ApiApp {

    public static void main(String[] args) throws Exception {
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApiImpl personApi = applicationContext.getBean(PersonApiImpl.class);
        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
        String queryQueue = personApi.getQueryQueue();

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queryQueue, true, false, false, null); // Назначаем очередь
        }

        // пишем взаимодействие с PersonApi
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите команду: SAVE, DELETE или FIND_ALL. Для завершения QUIT");
            String command = scanner.nextLine();
            if (command.equals("SAVE")) {
                System.out.println("Введите через пробел id, name, last name и middle name " +
                        "(например: 1 Ivan Ivanov Ivanovich)");
                String[] person = scanner.nextLine().split(" ");
                personApi.savePerson(Long.parseLong(person[0]), person[1], person[2], person[3]);
            } else if (command.equals("DELETE")) {
                System.out.println("Введите id для удаления записи из базы данных (например: 1)");
                personApi.deletePerson(Long.parseLong(scanner.nextLine()));
            } else if (command.equals("FIND_ALL")) {
                System.out.println(personApi.findAll());
            } else if (command.equals("QUIT")) {
                personApi.sendMessageToQueue("QUIT");
                scanner.close();
                return;
            }
        }

    }
}
