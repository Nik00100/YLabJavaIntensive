package snils_validator;

public class SnilsValidatorTest {
    public static void main(String[] args) {
        System.out.println("Проверка 01468870570: " + new SnilsValidatorImpl().validate("01468870570"));//false
        System.out.println("Проверка 90114404441: " + new SnilsValidatorImpl().validate("90114404441"));//true
    }
}
