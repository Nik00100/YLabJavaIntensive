package file_sort;

import java.io.*;
import java.util.*;

public class Sorter {
    // размер промежуточного файла
    private static final int CHUNK_SIZE = 100000000;

    public File sortFile(File dataFile) throws IOException {
        /// Создаем временную папку для хранения промежуточных файлов.
        File tmpDir = new File("temporary");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        // Разбиваем исходный файл на несколько файлов меньшего размера.
        List<File> chunkFiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            int chunkNum = 0;
            List<Long> chunk = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                chunk.add(Long.parseLong(line));
                if (chunk.size() == CHUNK_SIZE) {
                    // Отсортировать и записать промежуточный файл.
                    Collections.sort(chunk);
                    File chunkFile = new File(tmpDir, "chunk" + chunkNum + ".txt");
                    try (PrintWriter writer = new PrintWriter(chunkFile)) {
                        for (Long number : chunk) {
                            writer.println(number);
                        }
                    }
                    chunkFiles.add(chunkFile);
                    chunkNum++;
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) {
                // Отсортировать и записать промежуточный файл.
                Collections.sort(chunk);
                File chunkFile = new File(tmpDir, "chunk" + chunkNum + ".txt");
                try (PrintWriter writer = new PrintWriter(chunkFile)) {
                    for (Long number : chunk) {
                        writer.println(number);
                    }
                }
                chunkFiles.add(chunkFile);
            }
        }

        // Объединяем отсортированные файлы в один большой отсортированный файл.
        File sortedFile = new File("src/main/resources/sorted.txt");
        PriorityQueue<Scanner> heap = new PriorityQueue<>(Comparator.comparingLong(scanner -> scanner.nextLong()));
        for (File chunkFile : chunkFiles) {
            Scanner scanner = new Scanner(new FileInputStream(chunkFile));
            if (scanner.hasNextLong()) {
                heap.offer(scanner);
            }
        }
        try (PrintWriter writer = new PrintWriter(sortedFile)) {
            while (!heap.isEmpty()) {
                Scanner scanner = heap.poll();
                long number = scanner.nextLong();
                writer.println(number);
                if (scanner.hasNextLong()) {
                    heap.offer(scanner);
                } else {
                    scanner.close();
                }
            }
        }

        // Удаляем временные файлы.
        for (File chunkFile : chunkFiles) {
            chunkFile.delete();
        }
        tmpDir.delete();

        return sortedFile;
    }
}
