package taskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.util.List;

public abstract class taskList {
    protected List<task> tasks;

    public taskList() {
        this.tasks = new java.util.ArrayList<>();
    }

    public void addTask(task task) {
        tasks.add(task);
    }

    public void removeTask(task task) {
        tasks.remove(task);
    }

    public void editTask(task task, String name, String description, LocalDate dueDate) {
        task.setName(name);
        task.setDescription(description);
        task.setDueDate(dueDate);
    }

    public List<task> getTasks() {
        return tasks;
    }

    // Abstract method for subclasses to implement (e.g., custom display logic)
    public abstract void displayTasks();
}
