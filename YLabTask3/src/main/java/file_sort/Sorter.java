package file_sort;

import java.io.*;
import java.util.*;

public class Sorter {
    // размер промежуточного файла
    private static final int CHUNK_SIZE = 10000000;
    private static final String TMP_PREFIX = "temporary";

    public File sortFile(File dataFile) throws IOException {
        List<File> chunks = createChunks(dataFile);
        File output = new File("src/main/resources/sorted");
        mergeSort(chunks, output);
        deleteTempFiles(chunks);
        return output;
    }

    private List<File> createChunks(File dataFile) throws IOException {
        List<File> chunks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            List<Long> chunkData = new ArrayList<>();
            int chunkCount = 0;
            while ((line = reader.readLine()) != null) {
                chunkData.add(Long.parseLong(line));
                if (chunkData.size() == CHUNK_SIZE) {
                    chunkData.sort(Long::compare);
                    File chunkFile = File.createTempFile(TMP_PREFIX, null);
                    try (PrintWriter writer = new PrintWriter(chunkFile)) {
                        chunkData.forEach(writer::println);
                    }
                    chunks.add(chunkFile);
                    chunkData.clear();
                    chunkCount++;
                }
            }
            if (!chunkData.isEmpty()) {
                chunkData.sort(Long::compare);
                File chunkFile = File.createTempFile(TMP_PREFIX, null);
                try (PrintWriter writer = new PrintWriter(chunkFile)) {
                    chunkData.forEach(writer::println);
                }
                chunks.add(chunkFile);
            }
        }
        return chunks;
    }

    private void mergeSort(List<File> chunks, File outputFile) throws IOException {
        List<BufferedReader> readers = new ArrayList<>();
        for (File chunk : chunks) {
            readers.add(new BufferedReader(new FileReader(chunk)));
        }
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            PriorityQueue<ChunkReader> queue = new PriorityQueue<>();
            for (BufferedReader reader : readers) {
                ChunkReader chunkReader = new ChunkReader(reader);
                if (chunkReader.hasNext()) {
                    queue.add(chunkReader);
                }
            }
            while (!queue.isEmpty()) {
                ChunkReader chunkReader = queue.poll();
                writer.println(chunkReader.next());
                if (chunkReader.hasNext()) {
                    queue.add(chunkReader);
                }
            }
        }
    }

    private void deleteTempFiles(List<File> files) {
        for (File file : files) {
            file.delete();
        }
    }

    public class ChunkReader implements Comparable<ChunkReader> {
        private final BufferedReader reader;
        private String currentLine;

        public ChunkReader(BufferedReader reader) {
            this.reader = reader;
            try {
                this.currentLine = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            return currentLine != null;
        }

        public long next() throws IOException {
            long result = Long.parseLong(currentLine);
            currentLine = reader.readLine();
            if (currentLine == null) {
                reader.close();
            }
            return result;
        }

        @Override
        public int compareTo(ChunkReader o) {
            return Long.compare(Long.parseLong(currentLine), Long.parseLong(o.currentLine));
        }
    }
}
