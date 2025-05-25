package model;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String deadline;
    private int hours;
    private int priority; // 1 to 5 (1 = High, 5 = Low)
    private boolean completed;

    public Task(String title, String deadline, int hours, int priority) {
        this.title = title;
        this.deadline = deadline;
        this.hours = hours;
        this.priority = priority;
        this.completed = false;
    }

    public String getTitle() { return title; }
    public String getDeadline() { return deadline; }
    public int getHours() { return hours; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setTitle(String title) { this.title = title; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public void setHours(int hours) { this.hours = hours; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        // Serialize the task as a CSV line
        return title + "," + deadline + "," + hours + "," + priority + "," + completed;
    }

    public static Task fromString(String line) {
        // Deserialize a CSV line into a Task object
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }
        Task task = new Task(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])
        );
        task.setCompleted(Boolean.parseBoolean(parts[4]));
        return task;
    }
}
