import javax.swing.*;
import java.awt.*;

public class AIChatBotApp extends JFrame {
    private ChatManager chatManager;
    private ChatBot chatBot;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel chatAreaPanel;
    private JTextArea conversationArea;
    private JTextField inputField;
    private JButton sendButton;
    private DefaultListModel<String> chatListModel;
    private JList<String> chatList;
    
    public AIChatBotApp() {
        chatManager = new ChatManager();
        chatBot = new ChatBot();
        initializeUI();
        chatManager.createNewChat(); // Create initial chat
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JavaAI Assistant - AI Learning Chatbot");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel container = new JPanel(new BorderLayout());
        
        // Sidebar
        sidebarPanel = createSidebar();
        container.add(sidebarPanel, BorderLayout.WEST);
        
        // Main chat area
        chatAreaPanel = createChatArea();
        container.add(chatAreaPanel, BorderLayout.CENTER);
        
        add(container);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 42, 86));
        sidebar.setPreferredSize(new Dimension(200, 0));
        
        // Header
        JLabel headerLabel = new JLabel("JavaAI Assistant");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.add(headerLabel);
        
        // New chat button
        JButton newChatBtn = createSidebarButton("+ New Chat");
        newChatBtn.addActionListener(e -> newChat());
        sidebar.add(newChatBtn);
        sidebar.add(Box.createVerticalStrut(10));
        
        // Chat history label
        JLabel historyLabel = new JLabel("Chat History");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        historyLabel.setForeground(Color.WHITE);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.add(historyLabel);
        
        // Chat list
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        chatList.setBackground(new Color(52, 73, 120));
        chatList.setForeground(Color.WHITE);
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && chatList.getSelectedIndex() >= 0) {
                loadChat(chatList.getSelectedIndex());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(chatList);
        scrollPane.setPreferredSize(new Dimension(180, 200));
        sidebar.add(scrollPane);
        
        sidebar.add(Box.createVerticalStrut(10));
        
        // Clear chat button
        JButton clearChatBtn = createSidebarButton("Clear Chat");
        clearChatBtn.addActionListener(e -> clearCurrentChat());
        sidebar.add(clearChatBtn);
        
        sidebar.add(Box.createVerticalStrut(5));
        
        // Delete chat button
        JButton deleteChatBtn = createSidebarButton("Delete Chat");
        deleteChatBtn.setBackground(new Color(220, 53, 69));
        deleteChatBtn.addActionListener(e -> deleteCurrentChat());
        sidebar.add(deleteChatBtn);
        
        sidebar.add(Box.createVerticalGlue());
        
        // Exit button
        JButton exitBtn = createSidebarButton("Exit");
        exitBtn.setBackground(new Color(220, 53, 69));
        exitBtn.addActionListener(e -> System.exit(0));
        sidebar.add(exitBtn);
        sidebar.add(Box.createVerticalStrut(10));
        
        return sidebar;
    }
    
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(65, 95, 154));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });
        
        return btn;
    }
    
    private JPanel createChatArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("JavaAI - Your AI Learning Partner");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 42, 86));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Conversation area
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setFont(new Font("Arial", Font.PLAIN, 12));
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);
        conversationArea.setBackground(new Color(245, 245, 245));
        conversationArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(conversationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Quick questions
        JPanel quickQuestionsPanel = createQuickQuestionsPanel();
        panel.add(quickQuestionsPanel, BorderLayout.SOUTH);
        
        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 13));
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        sendButton.setBackground(new Color(52, 152, 219));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(80, 35));
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createQuickQuestionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel label = new JLabel("Quick Questions: ");
        label.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(label);
        
        for (String question : chatBot.getQuickQuestions()) {
            JButton btn = new JButton(question);
            btn.setFont(new Font("Arial", Font.PLAIN, 10));
            btn.setBackground(new Color(100, 150, 200));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btn.addActionListener(e -> {
                inputField.setText(question);
                sendMessage();
            });
            panel.add(btn);
        }
        
        return panel;
    }
    
    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;
        
        // Add user message
        Message userMessage = new Message("user", userInput);
        String currentSession = chatManager.getCurrentSessionId();
        chatManager.addMessage(currentSession, userMessage);
        
        // Add to UI
        appendToConversation("You (" + userMessage.getTimeString() + "): " + userInput);
        
        // Get bot response
        String botResponse = chatBot.getResponse(userInput);
        Message botMessage = new Message("bot", botResponse);
        chatManager.addMessage(currentSession, botMessage);
        
        // Add bot response with typing indicator
        appendToConversation("\nJavaAI (" + botMessage.getTimeString() + "):\n" + botResponse + "\n");
        
        inputField.setText("");
        scrollConversationToBottom();
    }
    
    private void appendToConversation(String text) {
        conversationArea.append(text + "\n\n");
    }
    
    private void scrollConversationToBottom() {
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }
    
    private void newChat() {
        chatManager.createNewChat();
        conversationArea.setText("");
        inputField.setText("");
        updateChatList();
    }
    
    private void clearCurrentChat() {
        String current = chatManager.getCurrentSessionId();
        chatManager.clearChat(current);
        conversationArea.setText("");
    }
    
    private void deleteCurrentChat() {
        String current = chatManager.getCurrentSessionId();
        chatManager.deleteChat(current);
        chatManager.createNewChat();
        conversationArea.setText("");
        inputField.setText("");
        updateChatList();
    }
    
    private void loadChat(int index) {
        String chatId = chatListModel.getElementAt(index);
        chatManager.getCurrentSessionId(); // Set current
        java.util.List<Message> messages = chatManager.getChat(chatId);
        
        conversationArea.setText("");
        for (Message msg : messages) {
            appendToConversation(msg.getSender().toUpperCase() + " (" + msg.getTimeString() + "):\n" + msg.getContent());
        }
        scrollConversationToBottom();
    }
    
    private void updateChatList() {
        chatListModel.clear();
        for (String chatId : chatManager.getAllChatIds()) {
            chatListModel.addElement(chatId);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AIChatBotApp app = new AIChatBotApp();
            app.setVisible(true);
        });
    }
}
