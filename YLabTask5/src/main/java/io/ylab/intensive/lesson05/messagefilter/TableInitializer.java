package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.*;

@Component
public class TableInitializer {
    @Value("${messagefilter.tablename}")
    private String tableName;
    @Value("${messagefilter.badwordsfile}")
    private String badWordsFile;

    private DataSource dataSource;

    @Autowired
    public TableInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initialize() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);

            if (!tables.next()) {
                createTable(connection);
                loadBadWords(connection);
            } else {
                loadBadWords(connection);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        }
    }

    private void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE " + tableName + " (" +
                            "id INT NOT NULL PRIMARY KEY, " +
                            "word VARCHAR(50) NOT NULL" +
                            ")"
            );
        } catch (SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        }
    }

    private void loadBadWords(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName);
            try (Scanner scanner = new Scanner(new File(badWordsFile))) {
                int id = 1;
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    statement.executeUpdate(
                            "INSERT INTO " + tableName + " (id, word) " +
                                    "VALUES (" + id + ", '" + word + "')"
                    );
                    id++;
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        }
    }

}