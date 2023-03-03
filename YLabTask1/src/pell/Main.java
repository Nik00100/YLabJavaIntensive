package pell;

import java.util.*;

public class Main {
    private static final int MAX_PELL_NUMBER = 30;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int[] pell = new int[MAX_PELL_NUMBER + 1];
            pell[0] = 0;
            pell[1] = 1;

            for (int i = 2; i <= n; i++) {
                pell[i] = 2 * pell[i - 1] + pell[i - 2];
            }

            System.out.println(pell[n]);

        } catch (InputMismatchException e) {
            System.out.println("Неверный формат ввода");
        }
    }
}
