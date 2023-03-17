package dated_map;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetedMapTest {
    private static DatedMapImpl datedMap;
    private static SimpleDateFormat formatter;

    private static void printKeyValue (String key) {
        System.out.println(String.format("Значение ключа '%s' - '%s'", key, datedMap.get(key)));
    }

    private static void printContainsKey (String key) {
        System.out.println(String.format("Содержит ли структура ключ '%s' - '%s'", key, datedMap.containsKey(key)));
    }

    private static void printLastInsertionDate (String key) {
        Date date = datedMap.getKeyLastInsertionDate(key);
        if (date != null) {
            System.out.println(String.format("Когда добавлен ключ '%s' - '%s'", key,
                    formatter.format(date)));
        } else {
            System.out.println(String.format("Ключ '%s' отсутствует в структуре", key));
        }
    }

    private static void printKeySet () {
        System.out.println(String.format("Все ключи, хранящиеся в структуре: %s",datedMap.keySet()));
    }

    public static void main(String[] args) {
        formatter = new SimpleDateFormat("HH:mm:ss dd-MMM-YYYY ");
        datedMap = new DatedMapImpl();

        datedMap.put("Hello", "World");
        datedMap.put("Moscow", "is the capital of Russia");
        datedMap.put("Lokomotiv", "Football Club");

        printKeySet();
        printKeyValue("Hello");
        printKeyValue("Moscow");
        printContainsKey("Moscow");
        printLastInsertionDate("Lokomotiv");

        datedMap.remove("Lokomotiv");

        printContainsKey("Lokomotiv");
        printLastInsertionDate("Lokomotiv");
        printKeySet();
    }
}
