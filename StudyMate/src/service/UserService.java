package service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserService {
    private static final String FILE = "data/users.txt";
    private final Map<String, String> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    private void loadUsers() {
        File file = new File(FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();  // Create "data" folder
            try {
                file.createNewFile();  // Create empty user file
            } catch (IOException e) {
                System.out.println("Error creating users file: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public boolean register(String username, String password, Scanner scanner) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return false;
        }

        // Password validation and retry
        while (!isValidPassword(password)) {
            System.out.println("\n❌ Password must be at least 8 characters and include:");
            System.out.println("   → At least one uppercase letter");
            System.out.println("   → At least one lowercase letter");
            System.out.println("   → At least one digit");
            System.out.println("   → At least one special character (!@#$%^&* etc.)");
            System.out.print("Enter a new password: ");
            password = scanner.nextLine();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }

        users.put(username, password);
        System.out.println("✅ User registered successfully!");
        return true;
    }

    public boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public boolean isUserExist(String username) {
        return users.containsKey(username);
    }

    // 🔒 Password validation logic
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
