package transliterator;

import java.util.*;

public class TransliteratorImpl implements Transliterator{
    private Map<Character, String> dictionary;

    public TransliteratorImpl() {
        dictionary = new HashMap<>();
        dictionary.put('А', "A");
        dictionary.put('Б', "B");
        dictionary.put('В', "V");
        dictionary.put('Г', "G");
        dictionary.put('Д', "D");
        dictionary.put('Е', "E");
        dictionary.put('Ё', "E");
        dictionary.put('Ж', "ZH");
        dictionary.put('З', "Z");
        dictionary.put('И', "I");
        dictionary.put('Й', "I");
        dictionary.put('К', "K");
        dictionary.put('Л', "L");
        dictionary.put('М', "M");
        dictionary.put('Н', "N");
        dictionary.put('О', "O");
        dictionary.put('П', "P");
        dictionary.put('Р', "R");
        dictionary.put('С', "S");
        dictionary.put('Т', "T");
        dictionary.put('У', "U");
        dictionary.put('Ф', "F");
        dictionary.put('Х', "KH");
        dictionary.put('Ц', "TS");
        dictionary.put('Ч', "CH");
        dictionary.put('Ш', "SH");
        dictionary.put('Щ', "SHCH");
        dictionary.put('Ы', "Y");
        dictionary.put('Ь', "");
        dictionary.put('Ъ', "IE");
        dictionary.put('Э', "E");
        dictionary.put('Ю', "IU");
        dictionary.put('Я', "IA");
    }

    @Override
    public String transliterate(String source) {
        StringBuilder sb = new StringBuilder();
        for (char ch : source.toCharArray()) {
            if (dictionary.containsKey(ch)) {
                sb.append(dictionary.get(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
