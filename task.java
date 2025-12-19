package taskManager;

import java.time.LocalDate;
import java.util.List;

public interface task {
    String getId();
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
    LocalDate getDueDate();
    void setDueDate(LocalDate dueDate);
    boolean isCompleted();
    void setCompleted(boolean completed);
    void markAsCompleted();  // Convenience method
    List<String> getComments();
    void addComment(String comment);
    String getDetails();  // For display purposes
}
