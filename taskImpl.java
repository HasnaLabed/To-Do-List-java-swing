package taskManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class taskImpl implements task {
    private String id;
    private String name;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private List<String> comments;

    public taskImpl(String name, String description, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
        this.comments = new ArrayList<>();
    }
    
    public static taskImpl fromCSV(String csvLine) {
        String[] parts = csvLine.split("\\|");
        if (parts.length < 6) return null;  // Invalid line
        taskImpl task = new taskImpl(parts[1], parts[2], LocalDate.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE));
        task.id = parts[0];  // Set ID directly (since it's final, we need to handle this carefully; assuming no duplicates)
        task.completed = Boolean.parseBoolean(parts[4]);
        if (!parts[5].isEmpty()) {
            task.comments = new ArrayList<>(Arrays.asList(parts[5].split(",")));
        }
        return task;
    }

    // Serialization to CSV
    public String toCSV() {
        String commentsStr = String.join(",", comments);
        return String.join("|", id, name, description, dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE), String.valueOf(completed), commentsStr);
    }
    
    @Override
    public void markAsCompleted() {
        this.completed = true;
    }
    @Override
    public void addComment(String comment) {
        comments.add(comment);}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getId() {
		return id;
	}

	@Override
	public String getDetails() {
        return String.format("ID: %s, Name: %s, Desc: %s, Due: %s, Completed: %s, Comments: %s",
                id, name, description, dueDate, completed, comments);
    
	}

   
}
