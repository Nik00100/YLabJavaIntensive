package snils_validator;

public class SnilsValidatorImpl implements SnilsValidator {
    private static final long[] indexes = {9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Override
    public boolean validate(String snils) {
        boolean answer = true;

        if (snils == null || snils.length() != 11) {
            answer = false;
        }

        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(snils.charAt(i))) {
                answer = false;
                break;
            }
        }

        if (answer) {
            long sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += indexes[i] * (snils.charAt(i) - '0');
            }

            long control;
            if (sum < 100) {
                control = sum;
            } else if (sum == 100) {
                control = 0;
            } else {
                long remainder = sum % 101;
                if (remainder == 100) {
                    control = 0;
                } else {
                    control = remainder;
                }
            }

            long lastDigits = (snils.charAt(9) - '0') * 10 + (snils.charAt(10) - '0');
            answer = lastDigits == control;
        }

        return answer;
    }
}
