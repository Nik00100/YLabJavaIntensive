package guess;

import java.util.*;

public class Main {
    private static final int TOTAL_ATTEMPTS = 10;

    public static void main(String[] args) {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = TOTAL_ATTEMPTS; // здесь задается количество попыток
        System.out.println("Я загадал число от 1 до 99. У тебя " + maxAttempts + " попыток угадать.");

        try (Scanner scanner = new Scanner(System.in)){
            while (maxAttempts > 0) {
                maxAttempts--;
                //if (maxAttempts == 0) break; // если при достижении 0 не требуется выводить надпись Мое число больше\меньше! У тебя осталось 0 попыток
                int quess = scanner.nextInt();
                if (quess > number) {
                    System.out.println(String.format("Мое число меньше! У тебя осталось %d попыток", maxAttempts));
                } else if (quess < number) {
                    System.out.println(String.format("Мое число больше! У тебя осталось %d попыток", maxAttempts));
                } else {
                    System.out.println(String.format("Ты угадал с %d попытки", TOTAL_ATTEMPTS - maxAttempts));
                    return;
                }
            }
            System.out.println("Ты не угадал");
        } catch (InputMismatchException e) {
            System.out.println("Неверный формат ввода");
        }
    }
}
