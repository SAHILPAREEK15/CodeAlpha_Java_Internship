import javax.swing.*;
import java.awt.*;

public class AddStudentPanel extends JPanel {
    private StudentRepository repository;
    private StudentGradeTrackerApp mainApp;
    
    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField rollNumberField;
    private JComboBox<String> branchCombo;
    private JSpinner semesterSpinner;
    private JTextField emailField;
    
    private JSpinner[] markSpinners;
    
    public AddStudentPanel(StudentRepository repository, StudentGradeTrackerApp mainApp) {
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
        
        JLabel titleLabel = new JLabel("Add Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student details section
        addSectionLabel(cardPanel, "Student Details", gbc, 0);
        
        gbc.gridy = 1;
        addFormField(cardPanel, "Student ID:", studentIdField = new JTextField(20), gbc);
        
        gbc.gridy = 2;
        addFormField(cardPanel, "Name:", nameField = new JTextField(20), gbc);
        
        gbc.gridy = 3;
        addFormField(cardPanel, "Roll Number:", rollNumberField = new JTextField(20), gbc);
        
        gbc.gridy = 4;
        String[] branches = {"CSE", "ECE", "Mechanical", "Civil", "Electrical"};
        branchCombo = new JComboBox<>(branches);
        addFormField(cardPanel, "Branch:", branchCombo, gbc);
        
        gbc.gridy = 5;
        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        addFormField(cardPanel, "Semester:", semesterSpinner, gbc);
        
        gbc.gridy = 6;
        addFormField(cardPanel, "Email:", emailField = new JTextField(20), gbc);
        
        // Marks section
        addSectionLabel(cardPanel, "Subject Marks", gbc, 7);
        
        String[] subjects = Student.getSubjects();
        markSpinners = new JSpinner[subjects.length];
        
        for (int i = 0; i < subjects.length; i++) {
            gbc.gridy = 8 + i;
            markSpinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            addFormField(cardPanel, subjects[i] + ":", markSpinners[i], gbc);
        }
        
        formPanel.add(cardPanel);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton addButton = new JButton("Add Student");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(52, 152, 219));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.addActionListener(e -> addStudent());
        
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(149, 165, 166));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(150, 40));
        resetButton.addActionListener(e -> resetForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);
        formPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void addSectionLabel(JPanel panel, String text, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(25, 42, 86));
        panel.add(label, gbc);
        gbc.gridwidth = 1;
    }
    
    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
    
    private void addStudent() {
        if (!validateFields()) {
            return;
        }
        
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String branch = (String) branchCombo.getSelectedItem();
        int semester = (int) semesterSpinner.getValue();
        String email = emailField.getText().trim();
        
        if (repository.studentExists(studentId)) {
            JOptionPane.showMessageDialog(this, "Student with ID " + studentId + " already exists!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Student student = new Student(studentId, name, rollNumber, branch, semester, email);
        
        String[] subjects = Student.getSubjects();
        for (int i = 0; i < subjects.length; i++) {
            int marks = (int) markSpinners[i].getValue();
            student.setMark(subjects[i], marks);
        }
        
        repository.addStudent(student);
        JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        resetForm();
        mainApp.refreshCurrentPanel();
    }
    
    private boolean validateFields() {
        if (studentIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (rollNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidEmail(emailField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void resetForm() {
        studentIdField.setText("");
        nameField.setText("");
        rollNumberField.setText("");
        branchCombo.setSelectedIndex(0);
        semesterSpinner.setValue(1);
        emailField.setText("");
        for (JSpinner spinner : markSpinners) {
            spinner.setValue(0);
        }
    }
}
