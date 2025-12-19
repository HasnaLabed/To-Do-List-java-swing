package taskManager;

import java.io.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class taskListImpl extends taskList {

    @Override
    public void displayTasks() {
        // In a GUI context, this could refresh the display; here it's a placeholder
        for (task task : tasks) {
            System.out.println(task.getDetails());
        }
    }

    // Filter by status (completed, not completed, abandoned - assuming abandoned means not completed and past due)
    public List<task> filterByStatus(boolean completed, boolean includeAbandoned) {
        return tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .filter(task -> !includeAbandoned || (!task.isCompleted() && task.getDueDate().isBefore(LocalDate.now())))
                .collect(Collectors.toList());
    }

    // Sort by due date
    public void sortByDueDate() {
        tasks.sort(Comparator.comparing(task::getDueDate));
    }

    // Search by keyword in name or description
    public List<task> search(String keyword) {
        return tasks.stream()
                .filter(task -> task.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Export to CSV file
    public void exportToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (task task : tasks) {
                if (task instanceof taskImpl) {
                    writer.write(((taskImpl) task).toCSV());
                    writer.newLine();
                }
            }
        }
    }

    // Import from CSV file
    public void importFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskImpl task = taskImpl.fromCSV(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
    }
}
