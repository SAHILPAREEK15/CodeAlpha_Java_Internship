import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String name;
    private String rollNumber;
    private String branch;
    private int semester;
    private String email;
    private Map<String, Integer> marks; // subject -> marks
    
    private static final String[] SUBJECTS = {"Mathematics", "Physics", "Chemistry", "DSA", "Java"};
    private static final int MAX_MARKS = 100;
    
    public Student(String studentId, String name, String rollNumber, String branch, int semester, String email) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.semester = semester;
        this.email = email;
        this.marks = new HashMap<>();
        
        for (String subject : SUBJECTS) {
            this.marks.put(subject, 0);
        }
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Map<String, Integer> getMarks() { return marks; }
    public void setMarks(Map<String, Integer> marks) { this.marks = marks; }
    
    public void setMark(String subject, int mark) {
        if (marks.containsKey(subject)) {
            marks.put(subject, mark);
        }
    }
    
    public int getMark(String subject) {
        return marks.getOrDefault(subject, 0);
    }
    
    public int getTotalMarks() {
        return marks.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getPercentage() {
        int total = getTotalMarks();
        int maxTotal = SUBJECTS.length * MAX_MARKS;
        return maxTotal > 0 ? (total * 100.0) / maxTotal : 0;
    }
    
    public double getAverage() {
        int total = getTotalMarks();
        return SUBJECTS.length > 0 ? (double) total / SUBJECTS.length : 0;
    }
    
    public String getGrade() {
        double percentage = getPercentage();
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }
    
    public static String[] getSubjects() {
        return SUBJECTS;
    }
    
    public static int getMaxMarks() {
        return MAX_MARKS;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", branch='" + branch + '\'' +
                ", semester=" + semester +
                ", email='" + email + '\'' +
                ", percentage=" + getPercentage() +
                ", grade='" + getGrade() + '\'' +
                '}';
    }
}
