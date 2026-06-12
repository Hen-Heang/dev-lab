package com.henheang.applying_project.studentmanagementsystem;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentManagementSystem is a SERVICE class.
 * It holds all the business logic — add, update, delete, search students.
 * <p>
 * [Spring] In Spring Boot, this becomes a @Service class.
 * The menu system (Scanner) is replaced by a @RestController
 * that receives HTTP requests (GET, POST, PUT, DELETE).
 * <p>
 * Current flow:  User types in terminal → Scanner → this class → HashMap (memory)
 * Spring flow:   User sends HTTP request → @RestController → @Service → @Repository (database)
 */

public class StudentManagementSystem {
    /**
     * HashMap stores all students in memory using their ID as the key.
     * Key   = Student ID (String)  → fast lookup, like searching by ID in a database
     * Value = Student object       → the full student data
     *
     * <p>
     * [Spring] In Spring Boot, this HashMap is replaced by a StudentRepository
     * that extends Repository<Student, String>.
     * Spring Data JPA handles all the save/find/delete automatically.
     */

    private final Map<String, Student> students;
    private final Scanner scanner;
    private final String DATA_FILE = "students.csv";

    /**
     * Constructor — runs once when the system starts.
     * It sets up the map, the scanner, and loads saved students from the file.
     * <p>
     * [Spring] Spring Boot handles startup automatically via @PostConstruct
     * or ApplicationRunner. You don't need to call start() manually.
     */

    public StudentManagementSystem() {
        students = new HashMap<>();
        scanner = new Scanner(System.in);
        loadStudentsFromFile();
    }

    /**
     * Entry point — starts the menu loop and keeps running until the user exits.
     * [Spring] In Spring Boot, there is no loop. The Spring server stays running
     * and waits for HTTP requests to come in.
     */
    public void start() {
        System.out.println("\n===================================");
        System.out.println("     🎓 Student Management System  ");
        System.out.println("===================================");

        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            // Switch expression (Java 14+) — cleaner than if/else chain
            // [Spring] Each case here becomes a separate API endpoint in Spring:
            //          case 1 (add)    → @PostMapping("/students")
            //          case 2 (view)   → @GetMapping("/students")
            //          case 3 (search) → @GetMapping("/students/{id}")
            //          case 4 (update) → @PutMapping("/students/{id}")
            //          case 5 (delete) → @DeleteMapping("/students/{id}")

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> addCourseToStudent();
                case 7 -> generateReports();
                case 8 -> {
                    saveStudentsToFile();
                    System.out.println("\n✅ Data saved. Goodbye!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Try again.");
            }
        }
    }

    //  Display menu for student management system
    private void displayMenu() {
        System.out.println("\n----------- MENU -----------");
        System.out.println("1️⃣  Add Student");
        System.out.println("2️⃣  View All Students");
        System.out.println("3️⃣  Search Student");
        System.out.println("4️⃣  Update Student");
        System.out.println("5️⃣  Delete Student");
        System.out.println("6️⃣  Add Course to Student");
        System.out.println("7️⃣  Generate Reports");
        System.out.println("8️⃣  Exit");
        System.out.println("----------------------------");
    }


    /**
     * Adds a new student to the system.
     * Steps: get ID → check duplicate → collect info → save to map
     * <p>
     * [Spring] This becomes a POST endpoint:
     *
     * @PostMapping("/students") public ResponseEntity<Student> addStudent(@RequestBody Student)
     */
    private void addStudent() {
        System.out.println("\n--- Add Student ---");

        // Prevent duplicate IDs — HashMap allows only one value per key
        // [Spring] The database enforces this with a UNIQUE constraint on the ID column
        String id = getStringInput("Enter Student ID: ");
        if (students.containsKey(id)) {
            System.out.println("⚠️ Student with ID " + id + " already exists.");
            return;
        }
//  Allow user input fields for new student.
        String name = getStringInput("Enter Name: ");
        int age = getIntInput("Enter Age: ");
        String email = getStringInput("Enter Email: ");
        double gpa = getDoubleInput();
// Create the Student object and store it in the map
        // students.put(key, value) — key is the ID for fast lookup later
        Student student = new Student(id, name, age, email, gpa);
        students.put(id, student);

//        after create student success.
        System.out.println("\n✅ Student added successfully!");
        printStudentHeader();
        System.out.println(student);
        printStudentFooter();
        System.out.println("----------------------------");
    }

    /**
     * Displays all students sorted by name (A → Z).
     * <p>
     * Stream pipeline used here:
     * students.values()  → get all Student objects from the map
     * .stream()          → prepare for functional operations
     * .sorted(...)       → sort by name alphabetically
     * .forEach(...)      → print each student
     * <p>
     * [Spring] This becomes: @GetMapping("/students") → return studentRepository.findAll()
     */

    //    Function for view all students.
    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");

//        Check if there are any students to display
        if (students.isEmpty()) {
            System.out.println("⚠️ No students found.");
            return;
        }

        printStudentHeader();
        students.values().stream()
                .sorted(Comparator.comparing(Student::getName))
                .forEach(System.out::println);
        printStudentFooter();
    }


    /**
     * Searches for one student by their exact ID.
     * HashMap.get(key) is O(1) — instant lookup, no need to loop through all students.
     * <p>
     * [Spring] This becomes: @GetMapping("/students/{id}")
     * studentRepository.findById(id) — same instant lookup via JPA
     */
    private void searchStudent() {
        System.out.println("\n--- Search Student ---");
//        Search student by ID
        String id = getStringInput("Enter Student ID: ");

        System.out.println("----------------------------");

//        Check if student exist with the given ID and display details

        Student student = students.get(id);
        if (student != null) {
            System.out.println("\n✅ Student found:");
            printStudentHeader();
            System.out.println(student);
            printStudentFooter();
        } else {
            System.out.println("⚠️ Student not found.");
        }
    }


    /**
     * Updates an existing student's information.
     * If the user presses Enter without typing, the current value is kept.
     * <p>
     * [Spring] This becomes: @PutMapping("/students/{id}")
     * The request body contains only the fields that changed.
     */

    private void updateStudent() {
        System.out.println("\n--- Update Student ---");

//        Logic of updating student is :
//        1. Get or set student id that have to updates.

        String id = getStringInput("Enter Student ID: ");

//      2. Check if student exist with the given ID and display current details
        Student student = students.get(id);
        if (student == null) {
            System.out.println("⚠️ Student not found.");
            return;
        }

        System.out.println("\nCurrent details:");
        printStudentHeader();
        System.out.println(student);
        printStudentFooter();

// 3 . Enter new data have to update.
        System.out.println("Enter new details (press Enter to keep current):");

        String name = getStringInput("Name [" + student.getName() + "]: ");
        if (!name.isEmpty()) {
            student.setName(name);
        }

        String ageStr = getStringInput("Age [" + student.getAge() + "]: ");
        if (!ageStr.isEmpty()) {
            try {
                student.setAge(Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid age format.");
            }
        }

        String email = getStringInput("Email [" + student.getEmail() + "]: ");
        if (!email.isEmpty()) {
            student.setEmail(email);
        }

        String gpaStr = getStringInput("GPA [" + student.getGpa() + "]: ");
        if (!gpaStr.isEmpty()) {
            try {
                student.setGpa(Double.parseDouble(gpaStr));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid GPA format.");
            }
        }

        System.out.println("\n✅ Student updated:");
        printStudentHeader();
        System.out.println(student);
        printStudentFooter();
    }

    /**
     * Deletes a student from the map by ID.
     * Map.remove(key) removes the entry and returns the deleted value (or null if not found).
     * <p>
     * [Spring] This becomes: @DeleteMapping("/students/{id}")
     * studentRepository.deleteById(id)
     */

    private void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        String id = getStringInput("Enter Student ID: ");

        Student removed = students.remove(id);
        if (removed != null) {
            System.out.println("\n🗑️ Student deleted:");
            printStudentHeader();
            System.out.println(removed);
            printStudentFooter();
        } else {
            System.out.println("⚠️ Student not found.");
        }
    }

    /**
     * Adds a course to a specific student.
     * The Student.addCourse() method checks for duplicates before adding.
     * <p>
     * [Spring] This could be: @PostMapping("/students/{id}/courses")
     */

    private void addCourseToStudent() {
        System.out.println("\n--- Add Course to Student ---");
        String id = getStringInput("Enter Student ID: ");

        // Check if student exist with the given ID
        // Get student id to update course
        Student student = students.get(id);
        if (student == null) {
            System.out.println("⚠️ Student not found.");
            return;
        }

        String course = getStringInput("Enter Course Name: ");
        student.addCourse(course);

        System.out.println("\n✅ Course added. Updated student:");
        printStudentHeader();
        System.out.println(student);
        printStudentFooter();
    }

    /**
     * Generates 3 types of reports using Java Stream API.
     * <p>
     * Stream API is a powerful tool for processing collections.
     * Think of it as a pipeline: source → filter/sort/group → result
     * <p>
     * [Spring] In Spring, you can do the same with:
     * - JPA queries: @Query("SELECT s FROM Student s ORDER BY s.gpa DESC")
     * - Or use Streams on the result list from the repository
     */

    private void generateReports() {

//        Option for view  report

        System.out.println("\n--- Reports ---");
        System.out.println("1. Students by GPA (High → Low)");
        System.out.println("2. Students by Age");
        System.out.println("3. Course Enrollment Report");

        int choice = getIntInput("Choose report: ");

        switch (choice) {

//            For case 1 : We use for generate report by GPA
//            What Comparator.comparing(Student::getGpa).reversed() use for :
//            Comparator.comparing(Student::getGpa) sorts students by GPA in ascending order.
//            .reversed() reverses the order to get descending (high to low).

            case 1 -> {
                System.out.println("\n📊 Students by GPA:");
                students.values().stream()
                        .sorted(Comparator.comparing(Student::getGpa).reversed())
                        .forEach(s -> System.out.printf(" %-15s | GPA: %.2f%n", s.getName(), s.getGpa()));
            }

//            For case 2 : We use for generate report by Age
//            What Comparator.comparing(Student::getAge) use for :
//            Comparator.comparing(Student::getAge) sorts students by age in ascending order (youngest to oldest).
            case 2 -> {
                System.out.println("\n📊 Students by Age:");
                students.values().stream()
                        .sorted(Comparator.comparing(Student::getAge))
                        .forEach(s -> System.out.printf(" %-15s | Age: %d%n", s.getName(), s.getAge()));
            }

            // REPORT 3: Count how many students are in each course
            //
            // Step-by-step breakdown:
            //   students.values().stream()           → stream of all Student objects
            //   .flatMap(s -> s.getCourses().stream()) → flatten: instead of [[A,B],[B,C]]
            //                                            we get a single stream [A, B, B, C]
            //   .collect(Collectors.groupingBy(      → group by course name and count
            //       course -> course,                    key   = course name
            //       Collectors.counting()))              value = number of students enrolled
            //
            // Result: Map<String, Long> e.g. {"Math" → 3, "Java" → 5}
            //
            // Then sort by count descending so the most popular course appears first


            case 3 -> {
                System.out.println("\n📊 Course Enrollment:");
                Map<String, Long> courseCount = students.values().stream()
                        .flatMap(s -> s.getCourses().stream())
                        .collect(Collectors.groupingBy(course -> course, Collectors.counting()));

                if (courseCount.isEmpty()) {
                    System.out.println("⚠️ No courses found.");
                } else {
                    courseCount.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .forEach(entry -> System.out.printf(" %-20s : %d students%n", entry.getKey(), entry.getValue()));
                }
            }
            default -> System.out.println("⚠️ Invalid choice.");
        }
    }

    // =====================================================================
    //  FILE I/O — Load and Save data using CSV file
    //  [Spring] Spring Data JPA + Hibernate replaces this entirely.
    //           save(), findAll(), deleteById() are auto-generated by JPA.
    // =====================================================================

    /**
     * Reads the CSV file line by line and rebuilds Student objects from each line.
     * Uses try-with-resources — the file is automatically closed after the block,
     * even if an exception happens. This prevents memory/resource leaks.
     * <p>
     * [Spring] @PostConstruct + JPA does this automatically on startup.
     */


    private void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                com.henheang.applying_project.studentmanagementsystem.Student student = com.henheang.applying_project.studentmanagementsystem.Student.fromCSV(line);
                if (student != null) {
                    students.put(student.getId(), student);
                }
            }
            System.out.println("📂 Loaded " + students.size() + " students from file.");
        } catch (FileNotFoundException e) {
            System.out.println("ℹ️ No existing data file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("⚠️ Error loading data: " + e.getMessage());
        }
    }

    /**
     * Writes all students to the CSV file, one student per line.
     * Each line is created by Student.toCSV().
     * <p>
     * [Spring] studentRepository.saveAll(students) — one line replaces this whole method.
     */

    private void saveStudentsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (com.henheang.applying_project.studentmanagementsystem.Student student : students.values()) {
                writer.println(student.toCSV());
            }
            System.out.println("💾 Saved " + students.size() + " students to file.");
        } catch (IOException e) {
            System.out.println("⚠️ Error saving data: " + e.getMessage());
        }
    }
    // =====================================================================
    //  INPUT HELPER METHODS
    //  These handle reading user input and retrying on bad input.
    //  [Spring] In Spring Boot, input validation uses @Valid + @NotBlank,
    //           @Min, @Max annotations on the model class instead.
    // =====================================================================

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid number. Try again.");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                System.out.print("Enter GPA: ");
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid number. Try again.");
            }
        }
    }

    private void printStudentHeader() {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-15s | %-3s | %-25s | %-4s | %-20s |%n",
                "ID", "Name", "Age", "Email", "GPA", "Courses");
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    private void printStudentFooter() {
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    static void main() {
        new StudentManagementSystem().start();
    }
}
