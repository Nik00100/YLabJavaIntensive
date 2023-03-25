package io.ylab.intensive.lesson04.movie;

import com.opencsv.*;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.*;
import java.util.List;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
    private DataSource dataSource;
    String[] columnMapping = {"year", "length", "title", "subject", "actors", "actress", "director", "popularity", "awards"};

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();
             Connection connection = dataSource.getConnection()) {

            csvReader.readNext();
            csvReader.readNext();

            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Movie.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean csvToBean = new CsvToBeanBuilder(csvReader).
                    withMappingStrategy(strategy).
                    build();

            List<Movie> movies = (List<Movie>) csvToBean.parse();

            String sql = "INSERT INTO movie (year, length, title, subject, actors, actress, director, popularity, awards) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Movie movie : movies) {
                insertMovie(movie, statement);
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (IOException | CsvValidationException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertMovie(Movie movie, PreparedStatement statement) throws SQLException {
        Object[] params = new Object[]{movie.getYear(), movie.getLength(), movie.getTitle(), movie.getSubject(),
                movie.getActors(), movie.getActress(), movie.getDirector(), movie.getPopularity(), movie.getAwards()};
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                if (params[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof String) {
                    statement.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) params[i]);
                }
            } else {
                statement.setNull(i + 1, Types.NULL);
            }
        }
    }

}
