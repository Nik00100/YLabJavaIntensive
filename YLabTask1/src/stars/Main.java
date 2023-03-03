package stars;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(template.charAt(0));
                }
                System.out.println();
            }
        } catch (InputMismatchException e) {
            System.out.println("Неверный формат ввода");
        }
    }
}
