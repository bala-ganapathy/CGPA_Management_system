import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentGUI extends JFrame {
    private JTextField nameField, subjectField;
    private JComboBox<String> gradeDropdown;
    private JButton addSubjectButton, submitButton, calculateButton;
    private JTextArea subjectList;
    private JLabel cgpaLabel;
    private ArrayList<String[]> subjects = new ArrayList<>();

    public StudentGUI() {
        setTitle("Student CGPA Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 20, 200, 30);
        add(nameField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(20, 70, 100, 30);
        add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(130, 70, 200, 30);
        add(subjectField);

        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setBounds(20, 120, 100, 30);
        add(gradeLabel);

        gradeDropdown = new JComboBox<>(new String[]{"O", "A+", "A", "B+", "B", "C", "RA"});
        gradeDropdown.setBounds(130, 120, 200, 30);
        add(gradeDropdown);

        addSubjectButton = new JButton("Add Subject");
        addSubjectButton.setBounds(350, 70, 120, 30);
        add(addSubjectButton);

        subjectList = new JTextArea();
        subjectList.setBounds(20, 160, 450, 100);
        subjectList.setEditable(false);
        add(subjectList);

        submitButton = new JButton("Submit Grades");
        submitButton.setBounds(50, 280, 150, 30);
        add(submitButton);

        calculateButton = new JButton("Calculate CGPA");
        calculateButton.setBounds(250, 280, 150, 30);
        add(calculateButton);

        cgpaLabel = new JLabel("CGPA: ");
        cgpaLabel.setBounds(20, 320, 300, 30);
        add(cgpaLabel);

        addSubjectButton.addActionListener(e -> addSubject());
        submitButton.addActionListener(e -> submitGrades());
        calculateButton.addActionListener(e -> calculateCGPA());
    }

    private void addSubject() {
        String subject = subjectField.getText();
        String grade = (String) gradeDropdown.getSelectedItem();

        if (subject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Subject cannot be empty!");
            return;
        }

        subjects.add(new String[]{subject, grade});
        subjectList.append("Subject: " + subject + ", Grade: " + grade + "\n");
        subjectField.setText("");
    }

    private void submitGrades() {
        String name = nameField.getText();

        if (subjects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one subject before submitting.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO students (name) VALUES (?) ON DUPLICATE KEY UPDATE name = name",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int studentId;
            if (rs.next()) {
                studentId = rs.getInt(1);
            } else {
                PreparedStatement ps2 = connection.prepareStatement("SELECT student_id FROM students WHERE name = ?");
                ps2.setString(1, name);
                rs = ps2.executeQuery();
                rs.next();
                studentId = rs.getInt("student_id");
            }

            PreparedStatement ps3 = connection.prepareStatement(
                "INSERT INTO grades (student_id, subject, grade) VALUES (?, ?, ?)");
            for (String[] subjectGrade : subjects) {
                ps3.setInt(1, studentId);
                ps3.setString(2, subjectGrade[0]);
                ps3.setString(3, subjectGrade[1]);
                ps3.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Grades submitted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void calculateCGPA() {
        String name = nameField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT student_id FROM students WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");

                PreparedStatement ps2 = connection.prepareStatement(
                    "SELECT grade FROM grades WHERE student_id = ?");
                ps2.setInt(1, studentId);
                rs = ps2.executeQuery();

                float totalPoints = 0;
                int count = 0;

                while (rs.next()) {
                    String grade = rs.getString("grade");
                    totalPoints += convertGradeToPoints(grade);
                    count++;
                }

                float cgpa = (count > 0) ? totalPoints / count : 0;

                PreparedStatement ps3 = connection.prepareStatement(
                    "UPDATE students SET cgpa = ? WHERE student_id = ?");
                ps3.setFloat(1, cgpa);
                ps3.setInt(2, studentId);
                ps3.executeUpdate();

                cgpaLabel.setText("CGPA: " + cgpa);
                JOptionPane.showMessageDialog(this, "CGPA calculated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private float convertGradeToPoints(String grade) {
        switch (grade) {
            case "O": return 10;
            case "A+": return 9;
            case "A": return 8;
            case "B+": return 7;
            case "B": return 6;
            case "C": return 5;
            case "RA": return 0;
            default: return 0;
        }
    }

    public static void main(String[] args) {
        new StudentGUI().setVisible(true);
    }
}
