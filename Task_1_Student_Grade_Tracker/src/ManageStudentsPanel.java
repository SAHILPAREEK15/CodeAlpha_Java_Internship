import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageStudentsPanel extends JPanel {
    private StudentRepository repository;
    private StudentGradeTrackerApp mainApp;
    
    private JTextField searchField;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    
    public ManageStudentsPanel(StudentRepository repository, StudentGradeTrackerApp mainApp) {
        this.repository = repository;
        this.mainApp = mainApp;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Manage Students");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Search and actions panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search();
            }
        });
        searchPanel.add(searchField);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        
        String[] columnNames = {"Student ID", "Name", "Roll Number", "Branch", "Semester", "Percentage", "Grade", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Actions column is editable
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(35);
        studentsTable.setBackground(Color.WHITE);
        studentsTable.getTableHeader().setBackground(new Color(52, 73, 120));
        studentsTable.getTableHeader().setForeground(Color.WHITE);
        studentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Custom cell renderer for actions
        studentsTable.getColumnModel().getColumn(7).setCellRenderer(new ActionButtonRenderer());
        studentsTable.getColumnModel().getColumn(7).setCellEditor(new ActionButtonEditor(repository, mainApp, this));
    }
    
    private void search() {
        String query = searchField.getText().trim();
        List<Student> results = query.isEmpty() ? 
            repository.getAllStudents() : 
            repository.searchStudents(query);
        
        updateTable(results);
    }
    
    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getStudentId(),
                student.getName(),
                student.getRollNumber(),
                student.getBranch(),
                student.getSemester(),
                String.format("%.1f%%", student.getPercentage()),
                student.getGrade(),
                "Edit / Delete"
            });
        }
    }
    
    public void refresh() {
        search();
    }
    
    // Cell renderer for action buttons
    private static class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            removeAll();
            
            JButton editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editBtn.setBackground(new Color(52, 152, 219));
            editBtn.setForeground(Color.WHITE);
            editBtn.setFocusPainted(false);
            editBtn.setPreferredSize(new Dimension(60, 25));
            
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setPreferredSize(new Dimension(60, 25));
            
            add(editBtn);
            add(deleteBtn);
            
            return this;
        }
    }
    
    // Cell editor for action buttons
    private static class ActionButtonEditor extends DefaultCellEditor {
        private StudentRepository repository;
        private StudentGradeTrackerApp mainApp;
        private ManageStudentsPanel panel;
        
        public ActionButtonEditor(StudentRepository repository, StudentGradeTrackerApp mainApp, ManageStudentsPanel panel) {
            super(new JTextField());
            this.repository = repository;
            this.mainApp = mainApp;
            this.panel = panel;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
            
            JButton editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            editBtn.setBackground(new Color(52, 152, 219));
            editBtn.setForeground(Color.WHITE);
            editBtn.setFocusPainted(false);
            editBtn.setPreferredSize(new Dimension(60, 25));
            editBtn.addActionListener(e -> {
                String studentId = (String) table.getValueAt(row, 0);
                editStudent(studentId);
            });
            
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Arial", Font.PLAIN, 10));
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setPreferredSize(new Dimension(60, 25));
            deleteBtn.addActionListener(e -> {
                String studentId = (String) table.getValueAt(row, 0);
                deleteStudent(studentId);
            });
            
            panel.add(editBtn);
            panel.add(deleteBtn);
            
            return panel;
        }
        
        private void editStudent(String studentId) {
            Student student = repository.getStudent(studentId);
            if (student != null) {
                EditStudentDialog dialog = new EditStudentDialog((JFrame) SwingUtilities.getWindowAncestor(panel), 
                    repository, student, mainApp, panel);
                dialog.setVisible(true);
            }
        }
        
        private void deleteStudent(String studentId) {
            int result = JOptionPane.showConfirmDialog(panel, 
                "Are you sure you want to delete this student?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                repository.deleteStudent(studentId);
                mainApp.refreshCurrentPanel();
            }
        }
    }
}
