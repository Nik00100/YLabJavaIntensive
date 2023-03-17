package file_sort;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        File dataFile = new Generator().generate("src/main/resources/data.txt", 10);
        System.out.println(new Validator(dataFile).isSorted()); // false
        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted()); // true
    }
}