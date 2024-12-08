import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CourseRegistrationAppAWT extends Frame {

    public CourseRegistrationAppAWT() {
        setTitle("Course Registration System");
        setSize(500, 700);
        setLayout(new GridLayout(3, 1));
        Font font = new Font("Arial", Font.BOLD, 24);
        setBackground(Color.LIGHT_GRAY);

        Button studentLoginButton = new Button("Student Login");
        studentLoginButton.setFont(font);
        studentLoginButton.setBackground(new Color(70, 130, 180)); 
        studentLoginButton.setForeground(Color.WHITE); 
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

    
        Font labelFont = new Font("Arial", Font.PLAIN, 24);
        setBackground(Color.WHITE); 

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(labelFont);
        TextField usernameField = new TextField();
        usernameField.setFont(labelFont);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(labelFont);
        TextField passwordField = new TextField();
        passwordField.setEchoChar('*');
        passwordField.setFont(labelFont);
        Button loginButton = new Button("Login");
        loginButton.setFont(labelFont);
        loginButton.setBackground(new Color(50, 205, 50));
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
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
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
                String s= CourseRegistrationSystem.registerCourse(studentId, courseCode);
                showMessageDialog(s);
            }else{
                showMessageDialog("Invalid course code");
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
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 500);
        TextArea textArea = new TextArea(message);
        textArea.setFont(new Font("Arial",Font.PLAIN,24));
        textArea.setEditable(false);
        dialog.add(textArea, BorderLayout.CENTER);

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        Panel buttonPanel = new Panel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private String promptInput(String message) {
        Dialog dialog = new Dialog(this, "Input", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 150);

        Label label = new Label(message);
        TextField inputField = new TextField(20);
        Button submitButton = new Button("Submit");

        submitButton.addActionListener(e -> dialog.dispose());

        dialog.add(label);
        dialog.add(inputField);
        dialog.add(submitButton);

        dialog.setVisible(true);
        return inputField.getText();
    }
}

class StudentRegistrationWindowAWT extends Frame {
    public StudentRegistrationWindowAWT() {
        setTitle("Student Registration");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        Font f = new Font("Arial",Font.PLAIN,24);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(f);
        TextField usernameField = new TextField();
        usernameField.setFont(f);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(f);
        TextField passwordField = new TextField();
        passwordField.setFont(f);
        passwordField.setEchoChar('*');
        Label fullNameLabel = new Label("Full Name:");
        fullNameLabel.setFont(f);
        TextField fullNameField = new TextField();
        fullNameField.setFont(f);
        Label emailLabel = new Label("Email:");
        emailLabel.setFont(f);
        TextField emailField = new TextField();
        emailField.setFont(f);
        Button registerButton = new Button("Register");
        registerButton.setFont(f);
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            if(CourseRegistrationSystem.getStudentId(username)==-1) {
                CourseRegistrationSystem.registerStudent(username,password,fullName,email);
                showMessageDialog("Student registered successfully!");
            } else {
                showMessageDialog("Student already registered try with other username or enter a valid username");
            }
            
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
        dialog.setSize(300, 200);
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
        setSize(400, 250);
        setLayout(new GridLayout(3, 2));
        setBackground(Color.WHITE);

        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Label usernameLabel = new Label("Admin Username:");
        usernameLabel.setFont(labelFont);
        TextField usernameField = new TextField();
        usernameField.setFont(labelFont);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(labelFont);
        TextField passwordField = new TextField();
        passwordField.setEchoChar('*');
        passwordField.setFont(labelFont);

        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
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
        Dialog dialog = new Dialog(this, "Error", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton);
        dialog.setVisible(true);
    }
}
class AdminDashboardWindowAWT extends Frame {
    public AdminDashboardWindowAWT() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLayout(new GridLayout(3, 1));
        setBackground(Color.LIGHT_GRAY);

        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        Button viewCoursesButton = new Button("View All Courses");
        viewCoursesButton.setFont(buttonFont);
        viewCoursesButton.setBackground(new Color(70, 130, 180)); 
        viewCoursesButton.setForeground(Color.WHITE);
        viewCoursesButton.addActionListener(e -> {
            List<String> courses = CourseRegistrationSystem.getAllCourses();
            showMessageDialog(courses.isEmpty() ? "No courses available." : "Available Courses:\n" + String.join("\n", courses));
        });

        Button addCourseButton = new Button("Add New Course");
        addCourseButton.setFont(buttonFont);
        addCourseButton.setBackground(new Color(50, 205, 50));
        addCourseButton.setForeground(Color.WHITE); 
        addCourseButton.addActionListener(e -> {
            String courseCode = promptInput("Enter Course Code:");
            String courseName = promptInput("Enter Course Name:");
            if (courseCode != null && courseName != null) {
            String result = CourseRegistrationSystem.addCourse(courseCode, courseName);
            if (result.equals("-1")) {
                showMessageDialog("Course already exists!");
            } else if (result.equals("0")) {
                showMessageDialog("Course added successfully!");
            } else {
                showMessageDialog("Error: " + result);
            }
        }

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(buttonFont);
        logoutButton.setBackground(new Color(220, 20, 60)); 
        logoutButton.setForeground(Color.WHITE); 
        logoutButton.addActionListener(e -> dispose());

        add(viewCoursesButton);
        add(addCourseButton);
        add(logoutButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void showMessageDialog(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        TextArea textArea = new TextArea(message);
        textArea.setFont(new Font("Arial", Font.PLAIN, 24));
        textArea.setEditable(false);
        dialog.add(textArea, BorderLayout.CENTER);

        Button closeButton = new Button("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        Panel buttonPanel = new Panel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private String promptInput(String message) {
        Dialog dialog = new Dialog(this, "Input", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 150);

        Label label = new Label(message);
        TextField inputField = new TextField(20);
        Button submitButton = new Button("Submit");

        submitButton.addActionListener(e -> dialog.dispose());

        dialog.add(label);
        dialog.add(inputField);
        dialog.add(submitButton);

        dialog.setVisible(true);
        return inputField.getText();
    }
}
