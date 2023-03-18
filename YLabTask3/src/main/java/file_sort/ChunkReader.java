package file_sort;

import java.io.*;

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