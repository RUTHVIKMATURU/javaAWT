import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationSystem {

    public static void registerStudent(String username, String password, String fullName, String email) {
        String query = "INSERT INTO students (username, password, full_name, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);

            stmt.executeUpdate();
            System.out.println("Student registered successfully.");
        } catch (SQLException e) {
            System.err.println("Error registering student: " + e.getMessage());
        }
    }

    public static boolean loginStudent(String username, String password) {
        String query = "SELECT student_id FROM students WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return false;
    }

    public static List<String> getAllCourses() {
        List<String> courses = new ArrayList<>();
        String query = "SELECT course_code, course_name FROM courses";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                courses.add(rs.getString("course_code") + " - " + rs.getString("course_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
        }
        return courses;
    }

    public static String registerCourse(int studentId, String courseCode) {
        String checkQuery = "SELECT COUNT(*) FROM registrations WHERE student_id = ? AND course_code = ?";
        String insertQuery = "INSERT INTO registrations (student_id, course_code) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
    
            // Check if the course is already registered
            checkStmt.setInt(1, studentId);
            checkStmt.setString(2, courseCode);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next() && rs.getInt(1) > 0) {
                return "Course already registered.";
            }
            insertStmt.setInt(1, studentId);
            insertStmt.setString(2, courseCode);
            insertStmt.executeUpdate();
    
            return "Course registered successfully.";
        } catch (SQLException e) {
            return "Error registering course: " + e.getMessage();
        }
    }
    

    public static List<String> getRegisteredCourses(int studentId) {
        List<String> courses = new ArrayList<>();
        String query = "SELECT c.course_code, c.course_name FROM registrations r " +
                       "JOIN courses c ON r.course_code = c.course_code " +
                       "WHERE r.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("course_code") + " - " + rs.getString("course_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving registered courses: " + e.getMessage());
        }
        return courses;
    }

    public static String addCourse(String courseCode, String courseName) {
        String checkQuery = "SELECT COUNT(*) FROM courses WHERE course_code = ? OR course_name = ?";
        String insertQuery = "INSERT INTO courses (course_code, course_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            checkStmt.setString(1, courseCode);
            checkStmt.setString(2, courseName);
            ResultSet rs = checkStmt.executeQuery();
    
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Course already exists. Cannot add duplicate.");
                return "-1";
            }
            insertStmt.setString(1, courseCode);
            insertStmt.setString(2, courseName);
            insertStmt.executeUpdate();
    
            System.out.println("Course added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
        return "0";
    }
    
    public static int getStudentId(String username) {
        String query = "SELECT student_id FROM students WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, username);
    
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("student_id");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student ID: " + e.getMessage());
        }
        return -1;
    }
    
    
}
