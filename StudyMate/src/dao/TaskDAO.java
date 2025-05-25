package dao;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private final String filePath;

    // Constructor takes username and builds path internally
    public TaskDAO(String username) {
        this.filePath = "src/data/tasks_" + username + ".txt";
        ensureFileExists();
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(Task.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks: " + e.getMessage());
        }

        return tasks;
    }

    public void saveTasks(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create data file: " + e.getMessage());
            }
        }
    }
}
