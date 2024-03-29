package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    @Autowired
    private DataSource dataSource;

    @Override
    public String queryForTable(String tableName) {
        String answer = null;
        try (Connection connection = dataSource.getConnection()) {
            // Получаем метаданные БД
            DatabaseMetaData metaData = connection.getMetaData();

            // Проверяем, что таблица существует в БД
            ResultSet rs = metaData.getTables(null, null, tableName, null);
            if (!rs.next()) {
                return null;
            }

            // Получаем список колонок таблицы
            List<String> columns = new ArrayList<>();
            rs = metaData.getColumns(null, null, tableName, null);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                columns.add(columnName);
            }

            // Формируем запрос на основе списка колонок
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ");
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(columns.get(i));
            }
            queryBuilder.append(" FROM ").append(tableName);
            answer = queryBuilder.toString();
        } catch (SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        }
        return answer;
    }

    @Override
    public List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            // Получаем метаданные БД
            DatabaseMetaData metaData = connection.getMetaData();
            // Получаем список таблиц
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(tableName);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обращении к базе данных");
        }
        return tables;
    }
}
