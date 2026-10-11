import java.util.*;

public class ChatBot {
    private Map<String, List<String>> knowledge;
    private Random random;
    
    public ChatBot() {
        this.knowledge = new HashMap<>();
        this.random = new Random();
        initializeKnowledge();
    }
    
    private void initializeKnowledge() {
        // Java Basics
        knowledge.put("java_basics", Arrays.asList(
            "Java is a high-level, object-oriented programming language. It follows the principle 'Write Once, Run Anywhere' (WORA).",
            "Key features of Java: Object-oriented, Platform-independent, Secure, Robust, Multithreaded, Simple syntax derived from C/C++.",
            "JVM (Java Virtual Machine) is an abstract computing machine that enables a computer to run Java programs and programs written in other languages.",
            "Java is used for building web applications, Android apps, desktop applications, and enterprise solutions.",
            "The main method is the entry point of any Java program: public static void main(String[] args) { }"
        ));
        
        // OOP Concepts
        knowledge.put("oop", Arrays.asList(
            "OOP (Object-Oriented Programming) is a programming paradigm based on objects and classes.",
            "The four pillars of OOP are: 1) Encapsulation - bundling data and methods, 2) Inheritance - extending classes, 3) Polymorphism - methods having multiple forms, 4) Abstraction - hiding complexity.",
            "Encapsulation: Wrapping data and methods together within a class, and hiding internal details using access modifiers.",
            "Inheritance allows a class to inherit properties and methods from another class, promoting code reuse.",
            "Polymorphism means 'many forms' - same method name can have different implementations (method overloading/overriding).",
            "Abstraction hides complex implementation details and shows only necessary features to the user.",
            "A class is a blueprint for creating objects. It defines properties and methods.",
            "An object is an instance of a class, representing a real-world entity."
        ));
        
        // DSA
        knowledge.put("dsa", Arrays.asList(
            "Data Structure is a way of organizing data efficiently. Common ones: Array, LinkedList, Stack, Queue, Tree, Graph, HashMap.",
            "Algorithm is a step-by-step procedure for solving a problem or accomplishing a task.",
            "Array: Fixed size, contiguous memory, O(1) access time, O(n) insertion/deletion.",
            "LinkedList: Dynamic size, non-contiguous memory, O(n) access time, O(1) insertion/deletion at known position.",
            "Stack follows LIFO (Last In First Out) - useful for function calls, undo operations.",
            "Queue follows FIFO (First In First Out) - useful for scheduling, BFS.",
            "Binary Search Tree: Left child < parent < right child. Useful for fast searching.",
            "Sorting algorithms: Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, Quick Sort, Heap Sort.",
            "Time Complexity: O(1) constant, O(log n) logarithmic, O(n) linear, O(n²) quadratic, O(n!) factorial.",
            "Space Complexity refers to the amount of memory an algorithm uses."
        ));
        
        // Internship & Placement
        knowledge.put("internship", Arrays.asList(
            "An internship is an opportunity to gain practical experience in a professional environment.",
            "Internships typically last 2-6 months and help you learn industry practices.",
            "During internship, focus on: understanding codebase, contributing to real projects, learning new technologies.",
            "CodeAlpha internship program provides experience in Java development, OOP, DSA, and web development.",
            "Key internship tips: Be proactive, ask questions, meet deadlines, communicate clearly, document your work.",
            "Internships help build your portfolio and improve your chances in placements.",
            "Common internship evaluation criteria: Code quality, problem-solving, teamwork, communication, initiative."
        ));
        
        // Placement & Interviews
        knowledge.put("placement", Arrays.asList(
            "Placement is the process of getting hired by a company after internship or graduation.",
            "Interview preparation: Data structures, algorithms, system design, behavioral questions, company research.",
            "Technical interview rounds: Coding, DSA, system design, database design.",
            "HR round focuses on: motivation, career goals, conflict resolution, soft skills.",
            "Common DSA interview topics: Arrays, Strings, LinkedList, Trees, Graphs, Dynamic Programming.",
            "Tips for interviews: Practice coding daily, understand concepts deeply, think aloud, optimize solutions.",
            "Behavioral questions like: Tell about yourself, Why this company, Biggest challenge, How you handle failure.",
            "Package negotiation: Research market rates, understand benefits, be professional in discussions."
        ));
        
        // General FAQ
        knowledge.put("general", Arrays.asList(
            "Hello! I'm JavaAI Assistant, your learning companion. Ask me about Java, OOP, DSA, internships, and placements!",
            "I can help you with Java basics, OOP concepts, Data Structures, Algorithms, and interview preparation.",
            "What would you like to learn today? Try asking about Java, OOP, DSA, Internship, or Placement!",
            "You can ask me specific questions or general topics. I'll try my best to help you learn!",
            "Feel free to ask follow-up questions if you need more details or clarification."
        ));
    }
    
    public String getResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase().trim();
        
        // Detect category
        String category = detectCategory(lowerMessage);
        
        // Get relevant response
        List<String> responses = knowledge.getOrDefault(category, knowledge.get("general"));
        return responses.get(random.nextInt(responses.size()));
    }
    
    private String detectCategory(String message) {
        // Java related keywords
        if (contains(message, "java", "jvm", "bytecode", "import", "package", "class", "method", "main")) {
            return "java_basics";
        }
        
        // OOP related keywords
        if (contains(message, "oop", "encapsulation", "inheritance", "polymorphism", "abstraction", "interface", "abstract")) {
            return "oop";
        }
        
        // DSA related keywords
        if (contains(message, "dsa", "data structure", "algorithm", "array", "linked list", "stack", "queue", "tree", "graph", "hash", "sort")) {
            return "dsa";
        }
        
        // Internship related keywords
        if (contains(message, "internship", "intern", "codeAlpha", "practical", "experience", "project")) {
            return "internship";
        }
        
        // Placement related keywords
        if (contains(message, "placement", "interview", "hiring", "job", "company", "offer", "package", "salary", "hr")) {
            return "placement";
        }
        
        // Default to general
        return "general";
    }
    
    private boolean contains(String message, String... keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    public List<String> getQuickQuestions() {
        return Arrays.asList(
            "What is Java?",
            "What is OOP?",
            "What is DSA?",
            "Difference between Array and LinkedList",
            "What is an Internship?",
            "How to prepare for interviews?"
        );
    }
}
