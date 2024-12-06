import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CourseRegistrationAppAWT extends Frame {

    public CourseRegistrationAppAWT() {
        setTitle("Course Registration System");
        setSize(500, 700);
        setLayout(new GridLayout(3, 1));

        // Apply font and background style
        Font font = new Font("Arial", Font.BOLD, 24);
        setBackground(Color.LIGHT_GRAY); // Set background color

        Button studentLoginButton = new Button("Student Login");
        studentLoginButton.setFont(font);
        studentLoginButton.setBackground(new Color(70, 130, 180)); // Set button color
        studentLoginButton.setForeground(Color.WHITE); // Set text color
        studentLoginButton.setPreferredSize(new Dimension(200, 60));
        studentLoginButton.addActionListener(e -> new StudentLoginWindowAWT());

        Button adminLoginButton = new Button("Admin Login");
        adminLoginButton.setFont(font);
        adminLoginButton.setBackground(new Color(70, 130, 180));
        adminLoginButton.setForeground(Color.WHITE);
        adminLoginButton.setPreferredSize(new Dimension(200, 60));
        adminLoginButton.addActionListener(e -> new AdminLoginWindowAWT());

        Button registerButton = new Button("Register as Student");
        registerButton.setFont(font);
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(200, 60));
        registerButton.addActionListener(e -> new StudentRegistrationWindowAWT());

        add(studentLoginButton);
        add(adminLoginButton);
        add(registerButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new CourseRegistrationAppAWT();
    }
}

class StudentLoginWindowAWT extends Frame {
    public StudentLoginWindowAWT() {
        setTitle("Student Login");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        // Apply font and color styles
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        setBackground(Color.WHITE); 

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(labelFont);
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(labelFont);
        TextField passwordField = new TextField();
        passwordField.setEchoChar('*');
        Button loginButton = new Button("Login");
        loginButton.setFont(labelFont);
        loginButton.setBackground(new Color(50, 205, 50)); // Green color
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (CourseRegistrationSystem.loginStudent(username, password)) {
                dispose();
                new StudentDashboardWindowAWT(username);
            } else {
                add(loginButton, "Invalid credentials!");
                new StudentLoginWindowAWT();
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 100);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button close = new Button("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);
        dialog.setVisible(true);
    }
}

class StudentDashboardWindowAWT extends Frame {
    public StudentDashboardWindowAWT(String username) {
        setTitle("Student Dashboard");
        setSize(400, 300);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Use FlowLayout for vertical arrangement

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        Button viewCoursesButton = new Button("View All Courses");
        viewCoursesButton.setFont(buttonFont);
        viewCoursesButton.setBackground(new Color(70, 130, 180));
        viewCoursesButton.setForeground(Color.WHITE);
        viewCoursesButton.addActionListener(e -> {
            List<String> courses = CourseRegistrationSystem.getAllCourses();
            showMessageDialog("Available Courses:\n" + String.join("\n", courses));
        });

        Button registerCourseButton = new Button("Register for a Course");
        registerCourseButton.setFont(buttonFont);
        registerCourseButton.setBackground(new Color(70, 130, 180));
        registerCourseButton.setForeground(Color.WHITE);
        registerCourseButton.addActionListener(e -> {
            String courseCode = promptInput("Enter Course Code to Register:");
            if (courseCode != null && !courseCode.isEmpty()) {
                int studentId = CourseRegistrationSystem.getStudentId(username);
                CourseRegistrationSystem.registerCourse(studentId, courseCode);
                showMessageDialog("Successfully registered for the course!");
            }
        });

        Button checkRegistrationButton = new Button("Check Registered Courses");
        checkRegistrationButton.setFont(buttonFont);
        checkRegistrationButton.setBackground(new Color(70, 130, 180));
        checkRegistrationButton.setForeground(Color.WHITE);
        checkRegistrationButton.addActionListener(e -> {
            int studentId = CourseRegistrationSystem.getStudentId(username);
            List<String> registeredCourses = CourseRegistrationSystem.getRegisteredCourses(studentId);
            if (registeredCourses.isEmpty()) {
                showMessageDialog("You have not registered for any courses yet.");
            } else {
                showMessageDialog("Registered Courses:\n" + String.join("\n", registeredCourses));
            }
        });

        add(viewCoursesButton);
        add(registerCourseButton);
        add(checkRegistrationButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 00);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button close = new Button("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);
        dialog.setVisible(true);
    }

    private String promptInput(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        TextField inputField = new TextField(20);
        dialog.add(inputField);
        Button submit = new Button("Submit");
        submit.addActionListener(e -> dialog.dispose());
        dialog.add(submit);
        dialog.setVisible(true);

        while (dialog.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        return inputField.getText();
    }
}


class StudentRegistrationWindowAWT extends Frame {
    public StudentRegistrationWindowAWT() {
        setTitle("Student Registration");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        passwordField.setEchoChar('*');
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Button registerButton = new Button("Register");

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            CourseRegistrationSystem.registerStudent(username, password, fullName, email);
            showMessageDialog("Student registered successfully!");
            dispose();
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(fullNameLabel);
        add(fullNameField);
        add(emailLabel);
        add(emailField);
        add(registerButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 100);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button close = new Button("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);
        dialog.setVisible(true);
    }
}
class AdminLoginWindowAWT extends Frame {
    public AdminLoginWindowAWT() {
        setTitle("Admin Login");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        Label usernameLabel = new Label("Admin Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        passwordField.setEchoChar('*');
        Button loginButton = new Button("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            // Replace with real admin authentication logic
            if ("admin".equals(username) && "password".equals(password)) {
                dispose();
                new AdminDashboardWindowAWT();
            } else {
                showMessageDialog("Invalid admin credentials!");
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 100);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button close = new Button("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);
        dialog.setVisible(true);
    }
}
class AdminDashboardWindowAWT extends Frame {
    public AdminDashboardWindowAWT() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Use FlowLayout for vertical arrangement

        Button viewCoursesButton = new Button("View All Courses");
        Button addCourseButton = new Button("Add New Course");

        viewCoursesButton.addActionListener(e -> {
            List<String> courses = CourseRegistrationSystem.getAllCourses();
            if (courses.isEmpty()) {
                showMessageDialog("No courses available.");
            } else {
                showMessageDialog("Available Courses:\n" + String.join("\n", courses));
            }
        });

        addCourseButton.addActionListener(e -> {
            String courseCode = promptInput("Enter Course Code:");
            String courseName = promptInput("Enter Course Name:");
            if (courseCode != null && !courseCode.isEmpty() && courseName != null && !courseName.isEmpty()) {
                CourseRegistrationSystem.addCourse(courseCode, courseName);
                showMessageDialog("Course added successfully!");
            } else {
                showMessageDialog("Invalid input. Please try again.");
            }
        });

        add(viewCoursesButton);
        add(addCourseButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 100);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button close = new Button("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(close);
        dialog.setVisible(true);
    }

    private String promptInput(String message) {
        Frame dialog = new Frame();
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        TextField inputField = new TextField(20);
        dialog.add(inputField);
        Button submit = new Button("Submit");
        submit.addActionListener(e -> dialog.dispose());
        dialog.add(submit);
        dialog.setVisible(true);

        while (dialog.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        return inputField.getText();
    }
}


