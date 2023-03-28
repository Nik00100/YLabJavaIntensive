package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);
    // Написать код демонстрации работы

    String map1 = "First";
    persistentMap.init(map1);
    persistentMap.put("Moscow", "Russia");
    persistentMap.put("Riga", "Latvia");
    persistentMap.put("Vilnus", "Litva");
    String key1 = "Moscow";
    System.out.println(String.format("Значение ключа %s: %s",key1, persistentMap.get(key1)));
    String key2 = "Riga";
    System.out.println(String.format("Содержится ли в PersistenceMap(%s) ключ %s: %s",map1, key2, persistentMap.containsKey(key2)));
    System.out.println(String.format("Все ключи в PersistenceMap(%s): %s",map1, persistentMap.getKeys()));
    String key3 = "Vilnus";
    persistentMap.remove(key3);
    System.out.println(String.format("После удаления %s все ключи в PersistenceMap(%s): %s",key3, map1, persistentMap.getKeys()));

    // инициализируем PersistenceMap(second)
    String map2 = "Second";
    persistentMap.init(map2);
    persistentMap.put("Mins", "Belarus");
    persistentMap.put("Erevan", "Armenia");
    System.out.println(String.format("Содержится ли в PersistenceMap(%s) ключ %s: %s",map2, key2, persistentMap.containsKey(key2)));


    // удаление данных из экземпляров PersistenceMap(first) и PersistenceMap(second)
    //persistentMap.clear();
    //persistentMap.init(map1);
    //persistentMap.clear();
  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
