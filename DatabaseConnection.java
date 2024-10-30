import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cgpa_management";
    private static final String USER = "root";
    private static final String PASSWORD = "java_pass";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resetDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            
            String sql = "DROP DATABASE IF EXISTS cgpa_management; " +
                         "CREATE DATABASE cgpa_management; " +
                         "USE cgpa_management; " +
                         "CREATE TABLE students (" +
                         "student_id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL UNIQUE, " +
                         "cgpa FLOAT DEFAULT 0); " +
                         "CREATE TABLE grades (" +
                         "grade_id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "student_id INT, " +
                         "subject VARCHAR(100), " +
                         "grade VARCHAR(2), " +
                         "FOREIGN KEY (student_id) REFERENCES students(student_id));";

            statement.executeUpdate(sql);
            System.out.println("Database reset and recreated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
