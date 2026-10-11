import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private StudentRepository repository;
    private StudentGradeTrackerApp mainApp;
    
    private JLabel totalStudentsLabel;
    private JLabel avgPercentageLabel;
    private JLabel highestScoreLabel;
    private JLabel lowestScoreLabel;
    private JLabel topGradeLabel;
    private JTable topPerformersTable;
    
    public DashboardPanel(StudentRepository repository, StudentGradeTrackerApp mainApp) {
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
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Statistics cards
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Top performers section
        JPanel topPerformersPanel = createTopPerformersPanel();
        contentPanel.add(topPerformersPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 20, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        totalStudentsLabel = createStatCard("Total Students", "0", new Color(52, 152, 219));
        avgPercentageLabel = createStatCard("Avg Percentage", "0%", new Color(46, 204, 113));
        highestScoreLabel = createStatCard("Highest Score", "0/500", new Color(155, 89, 182));
        lowestScoreLabel = createStatCard("Lowest Score", "0/500", new Color(230, 126, 34));
        topGradeLabel = createStatCard("A+ Students", "0", new Color(233, 30, 99));
        
        panel.add(totalStudentsLabel);
        panel.add(avgPercentageLabel);
        panel.add(highestScoreLabel);
        panel.add(lowestScoreLabel);
        panel.add(topGradeLabel);
        
        return panel;
    }
    
    private JLabel createStatCard(String title, String value, Color bgColor) {
        JLabel label = new JLabel();
        label.setLayout(new BoxLayout((JLabel) label, BoxLayout.Y_AXIS));
        label.setBackground(bgColor);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        label.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        
        Box box = Box.createVerticalBox();
        box.add(titleLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(valueLabel);
        
        label.setText("");
        return label;
    }
    
    private JPanel createTopPerformersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Top Performers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 42, 86));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Rank", "Name", "Roll Number", "Percentage", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        topPerformersTable = new JTable(model);
        topPerformersTable.setRowHeight(30);
        topPerformersTable.setBackground(Color.WHITE);
        topPerformersTable.getTableHeader().setBackground(new Color(52, 73, 120));
        topPerformersTable.getTableHeader().setForeground(Color.WHITE);
        topPerformersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(topPerformersTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refresh() {
        int totalStudents = repository.getTotalStudents();
        double avgPercentage = repository.getAveragePercentage();
        int highestScore = repository.getHighestScore();
        int lowestScore = repository.getLowestScore();
        long topGradeCount = repository.countGrade("A+");
        
        updateStatCard(totalStudentsLabel, "Total Students", String.valueOf(totalStudents));
        updateStatCard(avgPercentageLabel, "Avg Percentage", String.format("%.1f%%", avgPercentage));
        updateStatCard(highestScoreLabel, "Highest Score", highestScore + "/500");
        updateStatCard(lowestScoreLabel, "Lowest Score", lowestScore + "/500");
        updateStatCard(topGradeLabel, "A+ Students", String.valueOf(topGradeCount));
        
        // Update top performers table
        DefaultTableModel model = (DefaultTableModel) topPerformersTable.getModel();
        model.setRowCount(0);
        
        List<Student> topPerformers = repository.getTopPerformers(5);
        for (int i = 0; i < topPerformers.size(); i++) {
            Student student = topPerformers.get(i);
            model.addRow(new Object[]{
                i + 1,
                student.getName(),
                student.getRollNumber(),
                String.format("%.1f%%", student.getPercentage()),
                student.getGrade()
            });
        }
    }
    
    private void updateStatCard(JLabel card, String title, String value) {
        card.setText("");
        card.removeAll();
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        
        Box box = Box.createVerticalBox();
        box.add(titleLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(valueLabel);
        
        card.add(box);
        card.revalidate();
        card.repaint();
    }
}
