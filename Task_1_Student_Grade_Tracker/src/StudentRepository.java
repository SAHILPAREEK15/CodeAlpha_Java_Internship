import java.io.*;
import java.util.*;

public class StudentRepository {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + "/students.txt";
    
    private List<Student> students;
    
    public StudentRepository() {
        this.students = new ArrayList<>();
        createDataDirectoryIfNeeded();
        loadFromFile();
    }
    
    private void createDataDirectoryIfNeeded() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public void addStudent(Student student) {
        if (student != null && !studentExists(student.getStudentId())) {
            students.add(student);
            saveToFile();
        }
    }
    
    public void updateStudent(Student student) {
        if (student != null) {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getStudentId().equals(student.getStudentId())) {
                    students.set(i, student);
                    saveToFile();
                    return;
                }
            }
        }
    }
    
    public void deleteStudent(String studentId) {
        students.removeIf(s -> s.getStudentId().equals(studentId));
        saveToFile();
    }
    
    public Student getStudent(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(lowerQuery) ||
                student.getStudentId().toLowerCase().contains(lowerQuery) ||
                student.getRollNumber().toLowerCase().contains(lowerQuery)) {
                results.add(student);
            }
        }
        return results;
    }
    
    public boolean studentExists(String studentId) {
        return students.stream().anyMatch(s -> s.getStudentId().equals(studentId));
    }
    
    public List<Student> getTopPerformers(int count) {
        return students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getPercentage(), s1.getPercentage()))
                .limit(count)
                .toList();
    }
    
    public double getAveragePercentage() {
        return students.isEmpty() ? 0 : 
               students.stream()
                   .mapToDouble(Student::getPercentage)
                   .average()
                   .orElse(0);
    }
    
    public int getHighestScore() {
        return students.isEmpty() ? 0 :
               students.stream()
                   .mapToInt(Student::getTotalMarks)
                   .max()
                   .orElse(0);
    }
    
    public int getLowestScore() {
        return students.isEmpty() ? 0 :
               students.stream()
                   .mapToInt(Student::getTotalMarks)
                   .min()
                   .orElse(0);
    }
    
    public long countGrade(String grade) {
        return students.stream()
                .filter(s -> s.getGrade().equals(grade))
                .count();
    }
    
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            students = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            students = new ArrayList<>();
        }
    }
    
    public int getTotalStudents() {
        return students.size();
    }
}
