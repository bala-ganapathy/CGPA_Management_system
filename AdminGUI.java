import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminGUI extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;

    public AdminGUI() {
        setTitle("Admin - View Student CGPA");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel searchLabel = new JLabel("Enter Student Name:");
        searchLabel.setBounds(20, 20, 150, 30);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(180, 20, 150, 30);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(150, 60, 100, 30);
        add(searchButton);

        resultArea = new JTextArea();
        resultArea.setBounds(20, 100, 340, 150);
        resultArea.setEditable(false);
        add(resultArea);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStudentCGPA();
            }
        });
    }

    private void viewStudentCGPA() {
        String name = searchField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT s.name, s.cgpa, g.subject, g.grade " +
                "FROM students s " +
                "JOIN grades g ON s.student_id = g.student_id " +
                "WHERE s.name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            StringBuilder result = new StringBuilder("Student Name: " + name + "\n");

            boolean recordsFound = false;
            while (rs.next()) {
                recordsFound = true;
                result.append("Subject: ").append(rs.getString("subject"))
                      .append(", Grade: ").append(rs.getString("grade"))
                      .append("\n");
            }

            if (recordsFound) {
                ps = connection.prepareStatement("SELECT cgpa FROM students WHERE name = ?");
                ps.setString(1, name);
                rs = ps.executeQuery();
                if (rs.next()) {
                    result.append("CGPA: ").append(rs.getFloat("cgpa"));
                }
            } else {
                result.append("No records found for this student.");
            }

            resultArea.setText(result.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminGUI().setVisible(true);
    }
}
