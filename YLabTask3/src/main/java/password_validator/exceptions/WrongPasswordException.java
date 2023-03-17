package password_validator.exceptions;

public class WrongPasswordException extends Exception {
    private String text;

    public WrongPasswordException() {
        text = "Пароль неверный";
    }

    public WrongPasswordException(String message) {
        super(message);
        text = message;
    }

    @Override
    public String toString() {
        return "WrongPasswordException: " + text;
    }
}
