# Task 1: Student Grade Tracker

## Project Overview
A comprehensive Java Swing-based application for tracking student grades, calculating performance metrics, and generating detailed reports.

## Features
- **Dashboard**: View overall statistics and top performers
- **Add Student**: Register new students with personal and academic details
- **Manage Students**: Edit, delete, and search student records
- **Subject Marks**: Record marks for 5 subjects (Mathematics, Physics, Chemistry, DSA, Java)
- **Automatic Calculations**:
  - Total marks out of 500
  - Percentage calculation
  - Average marks per subject
  - Highest and lowest scores
  - Automatic grade assignment (A+, A, B+, B, C, D, F)
- **Reports**: Subject-wise performance and grade distribution
- **Data Persistence**: All data saved automatically to local files

## Technologies Used
- Java 17+
- Java Swing for GUI
- Object serialization for data persistence
- Collections Framework (ArrayList, HashMap)

## OOP Concepts Used
- **Encapsulation**: Student class with private fields and public getters/setters
- **Inheritance**: Panel classes extending JPanel
- **Polymorphism**: Method overriding in custom panels
- **Abstraction**: Data repository layer hiding storage details
- **Interfaces**: Implementing Serializable for object persistence

## Project Structure
```
Task_1_Student_Grade_Tracker/
├── src/
│   ├── StudentGradeTrackerApp.java      (Main application frame)
│   ├── Student.java                     (Student model class)
│   ├── StudentRepository.java           (Data persistence layer)
│   ├── DashboardPanel.java              (Dashboard view)
│   ├── AddStudentPanel.java             (Add student form)
│   ├── ManageStudentsPanel.java         (View/Edit/Delete students)
│   ├── EditStudentDialog.java           (Edit student dialog)
│   └── ReportsPanel.java                (Reports and statistics)
├── data/
│   └── students.txt                     (Student data file)
└── README.md
```

## Compilation & Execution

### Compile:
```bash
cd Task_1_Student_Grade_Tracker/src
javac *.java
```

### Run:
```bash
java StudentGradeTrackerApp
```

## How to Use

1. **Dashboard**: View overall student statistics and top 5 performers
2. **Add Student**: Click "Add Student" → Fill student details and marks → Click "Add Student"
3. **Manage Students**: 
   - Search students by name, ID, or roll number
   - Click "Edit" to modify student information
   - Click "Delete" to remove student
4. **Reports**: View subject-wise performance and grade distribution

## Input Validation
- Student ID must be unique
- Email format validation (user@domain)
- Marks must be between 0-100
- All required fields must be filled

## Data Storage
- Students are persisted in `data/students.txt`
- Data is saved automatically after each operation
- File is created automatically on first run

## Grading System
- A+ : 90% and above
- A  : 80%-89%
- B+ : 70%-79%
- B  : 60%-69%
- C  : 50%-59%
- D  : 40%-49%
- F  : Below 40%

## Limitations
- Current version supports max 100 subjects (extensible)
- No user authentication implemented
- Single user system

## Future Improvements
- Multi-user support with authentication
- Export reports to PDF/Excel
- Student performance charts and graphs
- Parent/Guardian access portal
- SMS/Email notifications for poor grades
- Historical grade tracking and trend analysis
