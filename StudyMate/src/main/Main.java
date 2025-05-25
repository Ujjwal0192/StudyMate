import service.TaskService;
import util.AuthService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("=== StudyMate ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                auth.register(scanner);
            } else if (choice.equals("2")) {
                if (auth.login(scanner)) {
                    String username = auth.getLoggedInUser();
                    TaskService taskService = new TaskService(username);
                    userMenu(scanner, taskService);
                }
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice!\n");
            }
        }
        scanner.close();
    }

    private static void userMenu(Scanner scanner, TaskService taskService) {
        while (true) {
            System.out.println("\n--- Task Menu ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task Completed");
            System.out.println("4. Delete Task");
            System.out.println("5. Show Progress");
            System.out.println("6. Sort Tasks by Deadline");
            System.out.println("7. Export Tasks to CSV");
            System.out.println("8. Logout");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> taskService.addTask(scanner);
                case "2" -> taskService.viewTasks();
                case "3" -> taskService.markTaskCompleted(scanner);
                case "4" -> taskService.deleteTask(scanner);
                case "5" -> taskService.showProgress();
                case "6" -> taskService.sortTasksByDeadline();
                case "7" -> {
                    System.out.print("Enter filename (or leave blank for default): ");
                    String filename = scanner.nextLine();
                    if (filename.isBlank()) {
                        taskService.exportTasksToCSV();
                    } else {
                        taskService.exportTasksToCSV(filename);
                    }
                }
                case "8" -> {
                    System.out.println("Logging out...\n");
                    return;
                }
                default -> System.out.println("Invalid option!\n");
            }
        }
    }
}
