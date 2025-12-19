package taskManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private taskListImpl taskList = new taskListImpl();
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, descField, dueDateField, searchField, commentField;
    private JButton addButton, editButton, deleteButton, completeButton, searchButton, 
                    sortButton, filterButton, exportButton, importButton;
    private JLabel statsLabel;
    
    // Soft feminine color palette
    private Color backgroundColor = new Color(255, 245, 250);
    private Color panelColor = new Color(255, 240, 245);
    private Color buttonColor = new Color(255, 182, 193);
    private Color buttonHoverColor = new Color(255, 105, 180);
    private Color textColor = new Color(102, 51, 102);
    private Color headerColor = new Color(230, 230, 250);
    private Color completedColor = new Color(220, 255, 220);

    public TaskManagerGUI() {
        setTitle("Task Manager ");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(backgroundColor);
        
        // Window icon
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/taskIcon.png")));
        } catch (Exception e) {
            // Continue without icon if not found
        }

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(255, 230, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(" Task Manager ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(199, 21, 133));

        JLabel subtitleLabel = new JLabel("Organize your tasks ", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(153, 102, 153));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
                "Task Details",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                textColor
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        
        inputPanel.add(createLabeledField("Task Name:", nameField = new JTextField(15)));
        nameField.setFont(fieldFont);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(createLabeledField("Description:", descField = new JTextField(15)));
        descField.setFont(fieldFont);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(createLabeledField("Due Date (YYYY-MM-DD):", dueDateField = new JTextField(15)));
        dueDateField.setFont(fieldFont);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(createLabeledField("Comment:", commentField = new JTextField(15)));
        commentField.setFont(fieldFont);
        
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(createLabeledField("Search:", searchField = new JTextField(15)));
        searchField.setFont(fieldFont);

        return inputPanel;
    }

    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(panelColor);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(textColor);
        
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setBackground(Color.WHITE);
        
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(backgroundColor);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Due Date", "Status"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? Boolean.class : String.class;
            }
        };
        
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskTable.getTableHeader().setBackground(headerColor);
        taskTable.getTableHeader().setForeground(textColor);
        taskTable.setGridColor(new Color(255, 218, 225));
        taskTable.setSelectionBackground(new Color(255, 182, 193));
        taskTable.setSelectionForeground(Color.WHITE);
        
        taskTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 250, 250) : new Color(255, 240, 245));
                }
                
                if (column == 4 && value instanceof Boolean && (Boolean)value) {
                    c.setBackground(completedColor);
                }
                
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(panelColor);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        statsLabel = new JLabel("Total Tasks: 0");
        statsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        statsLabel.setForeground(textColor);
        statsPanel.add(statsLabel);
        
        tablePanel.add(statsPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 240, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = createStyledButton(" Add", "Add new task");
        editButton = createStyledButton(" Edit", "Edit selected task");
        deleteButton = createStyledButton(" Delete", "Delete selected task");
        completeButton = createStyledButton(" Complete", "Mark as completed");
        searchButton = createStyledButton(" Search", "Search tasks");
        sortButton = createStyledButton(" Sort", "Sort by due date");
        filterButton = createStyledButton(" Filter", "Show completed only");
        exportButton = createStyledButton(" Export", "Export to file");
        importButton = createStyledButton(" Import", "Import from file");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);

        JButton showAllButton = createStyledButton(" Show All", "Show all tasks");
        showAllButton.addActionListener(e -> refreshTable());
        buttonPanel.add(showAllButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(buttonHoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 20, 147), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(buttonColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 105, 180), 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });

        return button;
    }

    private void addTask() {
        String name = nameField.getText();
        String desc = descField.getText();
        
        if (name.isEmpty()) {
            showMessage("Please enter task name!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            LocalDate dueDate = LocalDate.parse(dueDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            taskImpl task = new taskImpl(name, desc, dueDate);
            
            if (!commentField.getText().isEmpty()) {
                task.addComment(commentField.getText());
            }
            
            taskList.addTask(task);
            refreshTable();
            clearFields();
            
            showMessage("Task added successfully! ✅", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            showMessage("Invalid date format! Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            task task = taskList.getTasks().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
            if (task != null) {
                try {
                    String name = nameField.getText();
                    String desc = descField.getText();
                    LocalDate dueDate = LocalDate.parse(dueDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
                    
                    taskList.editTask(task, name, desc, dueDate);
                    
                    if (!commentField.getText().isEmpty()) {
                        task.addComment(commentField.getText());
                    }
                    
                    refreshTable();
                    clearFields();
                    showMessage("Task updated successfully! ", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    showMessage("Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            showMessage("Please select a task to edit", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this task?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                task task = taskList.getTasks().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
                if (task != null) {
                    taskList.removeTask(task);
                    refreshTable();
                    showMessage("Task deleted successfully! ", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            showMessage("Please select a task to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void markComplete() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            task task = taskList.getTasks().stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
            if (task != null) {
                task.markAsCompleted();
                refreshTable();
                showMessage("Task marked as completed! ✅", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showMessage("Please select a task", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchTasks() {
        String keyword = searchField.getText();
        if (!keyword.isEmpty()) {
            List<task> results = taskList.search(keyword);
            refreshTable(results);
            showMessage("Found " + results.size() + " task(s) ", "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            refreshTable();
        }
    }

    private void sortTasks() {
        taskList.sortByDueDate();
        refreshTable();
        showMessage("Tasks sorted by due date ", "Sorted", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void filterTasks() {
        List<task> filtered = taskList.filterByStatus(true, false);
        refreshTable(filtered);
        showMessage("Showing completed tasks only ", "Filter", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportTasks() {
        try {
            taskList.exportToFile("tasks.txt");
            showMessage("Exported successfully to tasks.txt ", "Export Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showMessage("Export failed: " + e.getMessage(), "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importTasks() {
        try {
            taskList.importFromFile("tasks.txt");
            refreshTable();
            showMessage("Imported successfully from tasks.txt ", "Import Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showMessage("Import failed: " + e.getMessage(), "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        refreshTable(taskList.getTasks());
    }

    private void refreshTable(List<task> tasks) {
        tableModel.setRowCount(0);
        for (task task : tasks) {
            String status = task.isCompleted() ? "✅ Completed" : " In Progress";
            tableModel.addRow(new Object[]{
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted()
            });
        }
        
        updateStats(tasks);
    }

    private void updateStats(List<task> tasks) {
        long completed = tasks.stream().filter(task::isCompleted).count();
        statsLabel.setText("Total: " + tasks.size() + " | Completed: " + completed);
    }

    private void clearFields() {
        nameField.setText("");
        descField.setText("");
        dueDateField.setText("");
        commentField.setText("");
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Event listeners setup
    private void setupEventListeners() {
        addButton.addActionListener(e -> addTask());
        editButton.addActionListener(e -> editTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> markComplete());
        searchButton.addActionListener(e -> searchTasks());
        sortButton.addActionListener(e -> sortTasks());
        filterButton.addActionListener(e -> filterTasks());
        exportButton.addActionListener(e -> exportTasks());
        importButton.addActionListener(e -> importTasks());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagerGUI gui = new TaskManagerGUI();
            gui.setupEventListeners();
            gui.setVisible(true);
        });
    }
}
