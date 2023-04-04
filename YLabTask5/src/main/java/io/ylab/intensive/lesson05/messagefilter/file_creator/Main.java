package io.ylab.intensive.lesson05.messagefilter.file_creator;

import java.io.*;

public class Main {
    static void wordsToFile(String inputFilePath, String outputFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        FileWriter writer = new FileWriter(outputFilePath);

        String line;
        while ((line = reader.readLine()) != null) {
            // разбиваем строку на слова, разделенные пробелами и запятыми
            String[] words = line.split(",");
            for (String word : words) {
                if (!word.trim().contains(" ")) {
                    writer.write(word.trim() + "\n"); // записываем каждое слово на отдельной строке (слово должно быть без пробелов)
                }
            }
        }

        reader.close();
        writer.close();
    }

    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/given.txt";
        String outputFilePath = "src/main/resources/bad-words.txt";

        try {
            wordsToFile(inputFilePath, outputFilePath);
            System.out.println("Все записано.");
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
