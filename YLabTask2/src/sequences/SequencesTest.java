package sequences;

import java.io.*;

public class SequencesTest {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Введите тип последовательности латинскими заглавными буквами от A до J.");
                System.out.println("Затем введите число N >= 1 (количество элементов для вывода в консоль).");
                System.out.println("Далее нажмите Enter для вывода элементов данной последовательности в консоль. ");
                System.out.println("Либо для завершения программы нажмите Q.");
                System.out.println("Пример:");
                System.out.println("A 5");

                String s = reader.readLine();
                if (s.equals("Q")) {
                    System.out.println("Программа завершена.");
                    reader.close();
                    return;
                } else {
                    String[] split = s.split(" ");
                    if (split[0].length() == 1) {
                        int n = Integer.parseInt(split[1]);
                        if (n < 1) {
                            System.out.println("Неверный формат ввода. Попробуйте еще раз.");
                        }
                        char command = split[0].charAt(0);
                        SequencesImpl sequences = new SequencesImpl();
                        switch (command) {
                            case 'A':
                                sequences.a(n);
                                break;
                            case 'B':
                                sequences.b(n);
                                break;
                            case 'C':
                                sequences.c(n);
                                break;
                            case 'D':
                                sequences.d(n);
                                break;
                            case 'E':
                                sequences.e(n);
                                break;
                            case 'F':
                                sequences.f(n);
                                break;
                            case 'G':
                                sequences.g(n);
                                break;
                            case 'H':
                                sequences.h(n);
                                break;
                            case 'I':
                                sequences.i(n);
                                break;
                            case 'J':
                                sequences.j(n);
                                break;
                            default:
                                System.out.println("Неверный формат ввода. Попробуйте еще раз.");
                                break;
                        }
                    } else {
                        System.out.println("Неверный формат ввода. Попробуйте еще раз.");
                    }
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Неверный формат ввода. Попробуйте еще раз.");
            }
        }

    }
}
