package mult_table;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.println(String.format("%d x %d = %d", i, j, i * j));
            }
        }
    }
}
