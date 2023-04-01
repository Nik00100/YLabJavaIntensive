package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();

        TableInitializer tableInitializer = applicationContext.getBean(TableInitializer.class);
        tableInitializer.initialize(); // инициализируем таблицу со всеми нецензурными выражениями

        Censor censor = applicationContext.getBean(Censor.class);
        censor.rabbitListener(); // запускаем обработчик сообщений брокера
    }
}
