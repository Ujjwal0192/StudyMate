package util;

import service.UserService;

import java.util.Scanner;

public class AuthService {
    private final UserService userService;
    private String loggedInUser = null;

    public AuthService() {
        userService = new UserService();
    }

    public boolean register(Scanner scanner) {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();

        if (userService.isUserExist(username)) {
            System.out.println("Username already exists!\n");
            return false;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = userService.register(username, password, scanner);  // ✅ Fixed line
        if (success) {
            System.out.println("Registration successful!\n");
        } else {
            System.out.println("Registration failed.\n");
        }
        return success;
    }


    public boolean login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = userService.login(username, password);
        if (success) {
            loggedInUser = username;
            System.out.println("Login successful!\n");
        } else {
            System.out.println("Invalid credentials.\n");
        }
        return success;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }
}
