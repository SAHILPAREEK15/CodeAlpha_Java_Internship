import javax.swing.*;
import java.awt.*;

public class StudentGradeTrackerApp extends JFrame {
    private StudentRepository repository;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    
    private DashboardPanel dashboardPanel;
    private AddStudentPanel addStudentPanel;
    private ManageStudentsPanel manageStudentsPanel;
    private ReportsPanel reportsPanel;
    
    public StudentGradeTrackerApp() {
        repository = new StudentRepository();
        initializeUI();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student Grade Tracker");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel container = new JPanel(new BorderLayout());
        
        // Sidebar
        sidebarPanel = createSidebar();
        container.add(sidebarPanel, BorderLayout.WEST);
        
        // Main content area with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        dashboardPanel = new DashboardPanel(repository, this);
        addStudentPanel = new AddStudentPanel(repository, this);
        manageStudentsPanel = new ManageStudentsPanel(repository, this);
        reportsPanel = new ReportsPanel(repository);
        
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(addStudentPanel, "AddStudent");
        mainPanel.add(manageStudentsPanel, "ManageStudents");
        mainPanel.add(reportsPanel, "Reports");
        
        container.add(mainPanel, BorderLayout.CENTER);
        
        add(container);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 42, 86));
        sidebar.setPreferredSize(new Dimension(200, 0));
        
        // Header
        JLabel headerLabel = new JLabel("Student Grade Tracker");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        sidebar.add(headerLabel);
        sidebar.add(Box.createVerticalStrut(20));
        
        // Navigation buttons
        String[] menuItems = {"Dashboard", "Add Student", "Manage Students", "Reports"};
        String[] actionNames = {"Dashboard", "AddStudent", "ManageStudents", "Reports"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createNavButton(menuItems[i], actionNames[i]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Footer
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(180, 40));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", 
                "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(10));
        
        return sidebar;
    }
    
    private JButton createNavButton(String text, String actionName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setBackground(new Color(52, 73, 120));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(65, 95, 154));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 120));
            }
        });
        
        btn.addActionListener(e -> {
            cardLayout.show(mainPanel, actionName);
            refreshCurrentPanel();
        });
        
        return btn;
    }
    
    public void refreshCurrentPanel() {
        if (dashboardPanel != null) dashboardPanel.refresh();
        if (manageStudentsPanel != null) manageStudentsPanel.refresh();
        if (reportsPanel != null) reportsPanel.refresh();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeTrackerApp app = new StudentGradeTrackerApp();
            app.setVisible(true);
        });
    }
}
