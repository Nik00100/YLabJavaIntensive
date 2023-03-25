package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.sql.*;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
    try (Connection connection = dataSource.getConnection()) {
      // Открываем файл и парсим его построчно
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(data))) {
        String line;
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO numbers (val) VALUES (?)");
        int batchSize = 0;

        while ((line = bufferedReader.readLine()) != null) {
          preparedStatement.setLong(1, Long.parseLong(line.trim()));
          preparedStatement.addBatch();
          batchSize++;

          if (batchSize % 1000 == 0) { // выполняем batch-processing каждые 1000 записей
            preparedStatement.executeBatch();
            batchSize = 0;
          }
        }

        // Если остались необработанные данные, выполняем batch-processing еще раз
        if (batchSize > 0) {
          preparedStatement.executeBatch();
        }
      }

      // Сортируем и выбираем числа из таблицы в порядке убывания
      try (Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("SELECT val FROM numbers ORDER BY val DESC")) {
        File sortedFile = new File("src/main/resources/sorted.txt");

        // Записываем отсортированные числа в новый файл
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sortedFile))) {
          while (resultSet.next()) {
            bufferedWriter.write(Long.toString(resultSet.getLong("val")));
            bufferedWriter.newLine();
          }
        }

        return sortedFile;
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
