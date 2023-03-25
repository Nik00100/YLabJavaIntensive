package io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {
  
  private DataSource dataSource;
  private String mapName;

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    this.mapName = name;
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "SELECT COUNT(*) FROM persistent_map WHERE map_name = ? AND KEY = ?");
    statement.setString(1, mapName);
    statement.setString(2, key);
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    int count = resultSet.getInt(1);
    resultSet.close();
    statement.close();
    connection.close();
    return count > 0;
  }

  @Override
  public List<String> getKeys() throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "SELECT KEY FROM persistent_map WHERE map_name = ?");
    statement.setString(1, mapName);
    ResultSet resultSet = statement.executeQuery();
    List<String> keys = new ArrayList<>();
    while (resultSet.next()) {
      keys.add(resultSet.getString(1));
    }
    resultSet.close();
    statement.close();
    connection.close();
    return keys;
  }

  @Override
  public String get(String key) throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "SELECT value FROM persistent_map WHERE map_name = ? AND KEY = ?");
    statement.setString(1, mapName);
    statement.setString(2, key);
    ResultSet resultSet = statement.executeQuery();
    String value = null;
    if (resultSet.next()) {
      value = resultSet.getString(1);
    }
    resultSet.close();
    statement.close();
    connection.close();
    return value;
  }

  @Override
  public void remove(String key) throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM persistent_map WHERE map_name = ? AND KEY = ?");
    statement.setString(1, mapName);
    statement.setString(2, key);
    statement.executeUpdate();
    statement.close();
    connection.close();
  }

  @Override
  public void put(String key, String value) throws SQLException {
    if (containsKey(key)) {
      remove(key);
    }
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO persistent_map (map_name, KEY, value) VALUES (?, ?, ?)");
    statement.setString(1, mapName);
    statement.setString(2, key);
    statement.setString(3, value);
    statement.executeUpdate();
    statement.close();
    connection.close();
  }

  @Override
  public void clear() throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM persistent_map WHERE map_name = ?");
    statement.setString(1, mapName);
    statement.executeUpdate();
    statement.close();
    connection.close();
  }
}
