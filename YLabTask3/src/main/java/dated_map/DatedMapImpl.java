package dated_map;

import java.util.*;

public class DatedMapImpl implements DatedMap{
    private Map<String, String> keyValue;
    private Map<String, Date> keyDate;

    public DatedMapImpl() {
        this.keyValue = new HashMap<>();
        this.keyDate = new HashMap<>();
    }

    @Override
    public void put(String key, String value) {
        System.out.println(String.format("Добавляем ключ '%s' и значение '%s' в структуру", key, value));
        Date date = new Date(System.currentTimeMillis());
        keyValue.put(key, value);
        keyDate.put(key, date);
    }

    @Override
    public String get(String key) {
        return keyValue.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return keyValue.containsKey(key);
    }

    @Override
    public void remove(String key) {
        System.out.println(String.format("Удаляем ключ '%s' и соответствующее ему значение из структуры", key));
        keyValue.remove(key);
        keyDate.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(keyValue.keySet());
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return keyDate.get(key);
    }

}
