package password_validator.exceptions;

public class WrongLoginException extends Exception {
    private String text;

    public WrongLoginException() {
        text = "“Логин неверный";
    }

    public WrongLoginException(String message) {
        super(message);
        text = message;
    }

    @Override
    public String toString() {
        return "WrongLoginException: " + text;
    }
}
