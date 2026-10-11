import javax.swing.*;
import java.awt.*;

public class EditStudentDialog extends JDialog {
    private Student student;
    private StudentRepository repository;
    private StudentGradeTrackerApp mainApp;
    private ManageStudentsPanel panel;
    
    private JTextField nameField;
    private JTextField emailField;
    private JSpinner semesterSpinner;
    private JSpinner[] markSpinners;
    
    public EditStudentDialog(JFrame parent, StudentRepository repository, Student student, 
                            StudentGradeTrackerApp mainApp, ManageStudentsPanel panel) {
        super(parent, "Edit Student", true);
        this.student = student;
        this.repository = repository;
        this.mainApp = mainApp;
        this.panel = panel;
        
        initializeUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Edit Student: " + student.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(25, 42, 86));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setPreferredSize(new Dimension(80, 25));
        nameField = new JTextField(student.getName(), 20);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        contentPanel.add(namePanel);
        
        // Email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setBackground(Color.WHITE);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setPreferredSize(new Dimension(80, 25));
        emailField = new JTextField(student.getEmail(), 20);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        contentPanel.add(emailPanel);
        
        // Semester
        JPanel semesterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        semesterPanel.setBackground(Color.WHITE);
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setPreferredSize(new Dimension(80, 25));
        semesterSpinner = new JSpinner(new SpinnerNumberModel(student.getSemester(), 1, 8, 1));
        semesterPanel.add(semesterLabel);
        semesterPanel.add(semesterSpinner);
        contentPanel.add(semesterPanel);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Marks section
        JLabel marksLabel = new JLabel("Subject Marks:");
        marksLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(marksLabel);
        
        String[] subjects = Student.getSubjects();
        markSpinners = new JSpinner[subjects.length];
        
        for (int i = 0; i < subjects.length; i++) {
            JPanel markPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            markPanel.setBackground(Color.WHITE);
            
            JLabel subjectLabel = new JLabel(subjects[i] + ":");
            subjectLabel.setPreferredSize(new Dimension(120, 25));
            
            int currentMark = student.getMark(subjects[i]);
            markSpinners[i] = new JSpinner(new SpinnerNumberModel(currentMark, 0, 100, 1));
            
            markPanel.add(subjectLabel);
            markPanel.add(markSpinners[i]);
            contentPanel.add(markPanel);
        }
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 12));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.addActionListener(e -> saveChanges());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        contentPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane);
    }
    
    private void saveChanges() {
        if (!nameField.getText().trim().isEmpty() && isValidEmail(emailField.getText().trim())) {
            student.setName(nameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setSemester((int) semesterSpinner.getValue());
            
            String[] subjects = Student.getSubjects();
            for (int i = 0; i < subjects.length; i++) {
                int mark = (int) markSpinners[i].getValue();
                student.setMark(subjects[i], mark);
            }
            
            repository.updateStudent(student);
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainApp.refreshCurrentPanel();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
