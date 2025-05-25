package service;

import dao.TaskDAO;
import model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TaskService {
    private final TaskDAO dao;
    private final List<Task> tasks;
    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter csvFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskService(String username) {
        String userTaskFile = "data/tasks_" + username + ".txt";
        dao = new TaskDAO(userTaskFile);
        tasks = dao.loadTasks();
    }

    public void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        System.out.print("Enter deadline (dd-MM-yyyy): ");
        String deadline = scanner.nextLine();

        System.out.print("Enter estimated hours: ");
        int hours = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter priority (1 = High, 5 = Low): ");
        int priority = Integer.parseInt(scanner.nextLine());

        tasks.add(new Task(title, deadline, hours, priority));
        dao.saveTasks(tasks);
        System.out.println("Task added successfully!\n");
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.\n");
            return;
        }

        System.out.println("\n--- Study Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            System.out.printf("%d. [%s] %s (Due: %s, Hours: %d, Priority: %d)%n",
                    i + 1,
                    t.isCompleted() ? "✔" : " ",
                    t.getTitle(),
                    t.getDeadline(),
                    t.getHours(),
                    t.getPriority());
        }
        System.out.println();
    }

    public void markTaskCompleted(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task number to mark as completed: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < tasks.size()) {
            tasks.get(idx).setCompleted(true);
            dao.saveTasks(tasks);
            System.out.println("Task marked as completed.\n");
        } else {
            System.out.println("Invalid task number.\n");
        }
    }

    public void deleteTask(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task number to delete: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx >= 0 && idx < tasks.size()) {
            tasks.remove(idx);
            dao.saveTasks(tasks);
            System.out.println("Task deleted.\n");
        } else {
            System.out.println("Invalid task number.\n");
        }
    }

    public void showProgress() {
        long completed = tasks.stream().filter(Task::isCompleted).count();
        int total = tasks.size();
        double percent = total == 0 ? 0 : (completed * 100.0) / total;
        System.out.printf("Progress: %.2f%% completed (%d/%d tasks)\n\n", percent, completed, total);
    }

    public void sortTasksByDeadline() {
        Collections.sort(tasks, Comparator.comparing(task -> LocalDate.parse(task.getDeadline(), inputFormatter)));
        System.out.println("Tasks sorted by deadline.\n");
    }

    public void exportTasksToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Title,Deadline,Estimated Hours,Priority,Status\n");

            for (Task task : tasks) {
                writer.append(escapeCSV(task.getTitle())).append(",");
                LocalDate date = LocalDate.parse(task.getDeadline(), inputFormatter);
                writer.append(date.format(csvFormatter)).append(",");
                writer.append(String.valueOf(task.getHours())).append(",");
                writer.append(String.valueOf(task.getPriority())).append(",");
                writer.append(task.isCompleted() ? "Completed" : "Pending").append("\n");
            }
            writer.flush();
            System.out.println("Tasks exported successfully to " + filename + "\n");
        } catch (IOException e) {
            System.out.println("Error exporting tasks: " + e.getMessage());
        }
    }

    public void exportTasksToCSV() {
        exportTasksToCSV("tasks_export.csv");
    }

    private String escapeCSV(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }
}
