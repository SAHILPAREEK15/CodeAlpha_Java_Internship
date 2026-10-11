import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private StudentRepository repository;
    private JTable subjectPerformanceTable;
    private JTable gradeDistributionTable;
    
    public ReportsPanel(StudentRepository repository) {
        this.repository = repository;
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
        
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 1, 20, 20));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        contentPanel.add(createSubjectPerformancePanel());
        contentPanel.add(createGradeDistributionPanel());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createSubjectPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Subject-wise Performance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(25, 42, 86));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Subject", "Total Students", "Average Marks", "Highest Marks", "Lowest Marks"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        subjectPerformanceTable = new JTable(model);
        subjectPerformanceTable.setRowHeight(30);
        subjectPerformanceTable.setBackground(Color.WHITE);
        subjectPerformanceTable.getTableHeader().setBackground(new Color(52, 73, 120));
        subjectPerformanceTable.getTableHeader().setForeground(Color.WHITE);
        subjectPerformanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(subjectPerformanceTable);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createGradeDistributionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Grade Distribution");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(25, 42, 86));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Grade", "Number of Students", "Percentage"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        gradeDistributionTable = new JTable(model);
        gradeDistributionTable.setRowHeight(30);
        gradeDistributionTable.setBackground(Color.WHITE);
        gradeDistributionTable.getTableHeader().setBackground(new Color(52, 73, 120));
        gradeDistributionTable.getTableHeader().setForeground(Color.WHITE);
        gradeDistributionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(gradeDistributionTable);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refresh() {
        updateSubjectPerformance();
        updateGradeDistribution();
    }
    
    private void updateSubjectPerformance() {
        DefaultTableModel model = (DefaultTableModel) subjectPerformanceTable.getModel();
        model.setRowCount(0);
        
        List<Student> students = repository.getAllStudents();
        String[] subjects = Student.getSubjects();
        
        for (String subject : subjects) {
            int totalStudents = students.size();
            if (totalStudents == 0) continue;
            
            int sum = 0;
            int highest = 0;
            int lowest = 100;
            
            for (Student student : students) {
                int mark = student.getMark(subject);
                sum += mark;
                highest = Math.max(highest, mark);
                lowest = Math.min(lowest, mark);
            }
            
            double average = (double) sum / totalStudents;
            
            model.addRow(new Object[]{
                subject,
                totalStudents,
                String.format("%.1f", average),
                highest,
                lowest
            });
        }
    }
    
    private void updateGradeDistribution() {
        DefaultTableModel model = (DefaultTableModel) gradeDistributionTable.getModel();
        model.setRowCount(0);
        
        String[] grades = {"A+", "A", "B+", "B", "C", "D", "F"};
        int totalStudents = repository.getTotalStudents();
        
        for (String grade : grades) {
            long count = repository.countGrade(grade);
            double percentage = totalStudents > 0 ? (count * 100.0) / totalStudents : 0;
            
            model.addRow(new Object[]{
                grade,
                count,
                String.format("%.1f%%", percentage)
            });
        }
    }
}
