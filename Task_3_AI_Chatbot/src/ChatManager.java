import java.io.*;
import java.util.*;

public class ChatManager {
    private static final String DATA_DIR = "data";
    private static final String CHATS_DIR = DATA_DIR + "/chats";
    
    private Map<String, List<Message>> chatSessions;
    private String currentSessionId;
    
    public ChatManager() {
        this.chatSessions = new HashMap<>();
        createDataDirectoryIfNeeded();
        loadChats();
    }
    
    private void createDataDirectoryIfNeeded() {
        new File(CHATS_DIR).mkdirs();
    }
    
    public String createNewChat() {
        String sessionId = "chat_" + System.currentTimeMillis();
        chatSessions.put(sessionId, new ArrayList<>());
        currentSessionId = sessionId;
        return sessionId;
    }
    
    public void addMessage(String sessionId, Message message) {
        chatSessions.putIfAbsent(sessionId, new ArrayList<>());
        chatSessions.get(sessionId).add(message);
        saveChat(sessionId);
    }
    
    public List<Message> getChat(String sessionId) {
        return new ArrayList<>(chatSessions.getOrDefault(sessionId, new ArrayList<>()));
    }
    
    public List<String> getAllChatIds() {
        return new ArrayList<>(chatSessions.keySet());
    }
    
    public void deleteChat(String sessionId) {
        chatSessions.remove(sessionId);
        new File(CHATS_DIR + "/" + sessionId + ".txt").delete();
    }
    
    public void clearChat(String sessionId) {
        chatSessions.put(sessionId, new ArrayList<>());
        saveChat(sessionId);
    }
    
    public String getCurrentSessionId() {
        if (currentSessionId == null) {
            currentSessionId = createNewChat();
        }
        return currentSessionId;
    }
    
    private void saveChat(String sessionId) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CHATS_DIR + "/" + sessionId + ".txt"))) {
            oos.writeObject(chatSessions.get(sessionId));
        } catch (IOException e) {
            System.err.println("Error saving chat: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadChats() {
        File dir = new File(CHATS_DIR);
        if (!dir.exists()) return;
        
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    String sessionId = file.getName().replace(".txt", "");
                    List<Message> messages = (List<Message>) ois.readObject();
                    chatSessions.put(sessionId, messages);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error loading chat: " + e.getMessage());
                }
            }
        }
    }
}
