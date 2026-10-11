import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String sender; // "user" or "bot"
    private String content;
    private long timestamp;
    
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    
    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(timestamp));
    }
    
    @Override
    public String toString() {
        return "[" + getTimeString() + "] " + sender + ": " + content;
    }
}
