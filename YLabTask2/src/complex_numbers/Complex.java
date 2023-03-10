package complex_numbers;

public interface Complex {
    /** Сложение комплексных чисел
     * @param z слагаемое
     * @return Complex
     * */
    Complex add(Complex z);

    /** Вычитание комплексных чисел
     * @param z вычитаемое
     * @return Complex
     * */
    Complex subtract(Complex z);

    /** Умножение комплексных чисел
     * @param z множитель
     * @return Complex
     * */
    Complex multiply(Complex z);

    /** Модуль комплексного числа z
     * @return double
     * */
    double abs();
}
