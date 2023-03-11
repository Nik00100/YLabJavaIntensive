package complex_numbers;

public class ComplexTest {
    public static void main(String[] args) {
        ComplexImpl a = new ComplexImpl(5);
        ComplexImpl b = new ComplexImpl(7, 11);


        System.out.println("Сложение комплексных чисел:");
        System.out.println(String.format("(%s) + (%s) = %s", a, b, a.add(b)));

        System.out.println();
        System.out.println("Вычитание комплексных чисел:");
        System.out.println(String.format("(%s) - (%s) = %s", a, b, a.subtract(b)));

        System.out.println();
        System.out.println("Умножение комплексных чисел:");
        System.out.println(String.format("(%s) * (%s) = %s", a, b, a.multiply(b)));

        System.out.println();
        System.out.println(String.format("Модуль комплексного числа: (%s) = %s", a, a.abs()));
    }
}
