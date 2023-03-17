package password_validator;

import password_validator.exceptions.WrongLoginException;
import password_validator.exceptions.WrongPasswordException;

import java.util.Scanner;

public class Login {
    private String regex = "^[a-zA-Z0-9_]+$";

    private boolean canLogin (String login, String password, String confirmPassword)
            throws WrongLoginException, WrongPasswordException {
        if (!login.matches(regex)) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        } else if (login.length() >= 20) {
            throw new WrongLoginException("Логин слишком длинный");
        } else if (!password.matches(regex)) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        } else if (password.length() >= 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        } else if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }
        return true;
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
        System.out.println("Введите логин, пароль и подтверждение пароля. Каждое значение вводите с новой строки.");
        String login = scanner.nextLine();
        String password = scanner.nextLine();
        String confirmPassword = scanner.nextLine();

        if (canLogin(login, password, confirmPassword)) {
            System.out.println("Вы успешно вошли в систему");
        }

        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        }
    }
}
