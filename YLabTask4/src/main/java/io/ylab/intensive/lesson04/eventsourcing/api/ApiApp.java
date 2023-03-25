package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import java.util.Scanner;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        PersonApiImpl personApi = new PersonApiImpl(connectionFactory);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите команду: SAVE, DELETE или FIND_ALL. Для завершения Q.");
            String command = scanner.nextLine();
            if (command.equals("SAVE")) {
                System.out.println("Введите через пробел id, name, last name и middle name " +
                        "(например: 1 Ivan Ivanov Ivanovich)");
                String[] person = scanner.nextLine().split(" ");
                personApi.savePerson(Long.parseLong(person[0]), person[1], person[2], person[2]);
            } else if (command.equals("DELETE")) {
                System.out.println("Введите id для удаления записи из базы данных (например: 1)");
                personApi.deletePerson(Long.parseLong(scanner.nextLine()));
            } else if (command.equals("FIND_ALL")) {
                System.out.println(personApi.findAll());
            } else if (command.equals("Q")) {
                scanner.close();
                return;
            }
        }

    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
