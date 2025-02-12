package quiz_application;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code Dashboard} class represents the admin panel.
 * It provides options to manage quiz questions and view reports.
 */

public class Dashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new {@code Dashboard} for the admin.
     */
    public Dashboard() {
        setTitle("Admin Panel");
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton reportButton = new JButton("Check Report");
        reportButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        reportButton.setBounds(102, 282, 363, 60);
        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        addQuestionButton.setBounds(1070, 282, 363, 60);
        JButton editQuestionButton = new JButton("Edit Question");
        editQuestionButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        editQuestionButton.setBounds(102, 471, 363, 60);
        JButton deleteQuestionButton = new JButton("Delete Question");
        deleteQuestionButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        deleteQuestionButton.setBounds(1070, 471, 363, 60);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        logoutButton.setBounds(587, 637, 363, 60);
        getContentPane().setLayout(null);

        getContentPane().add(reportButton);
        getContentPane().add(addQuestionButton);
        getContentPane().add(editQuestionButton);
        getContentPane().add(deleteQuestionButton);
        getContentPane().add(logoutButton);
        
        JLabel lblNewLabel = new JLabel("Dashboard");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 35));
        lblNewLabel.setBounds(646, 85, 343, 75);
        getContentPane().add(lblNewLabel);

        reportButton.addActionListener(e -> new Report());
        addQuestionButton.addActionListener(e -> new AddQuestion(this));
        editQuestionButton.addActionListener(e -> editSelectedQuestion());
        deleteQuestionButton.addActionListener(e -> deleteSelectedQuestion());

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });

        setVisible(true);
    }
    
    /**
     * Prompts the admin to enter a question ID and opens the edit question form.
     */
    
    private void editSelectedQuestion() {
        String questionId = JOptionPane.showInputDialog(this, "Enter Question ID to Edit:");
        if (questionId != null && !questionId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(questionId);
                new AddQuestion(this, id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Question ID. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Prompts the admin to enter a question ID and deletes the question.
     */
    private void deleteSelectedQuestion() {
        String questionId = JOptionPane.showInputDialog(this, "Enter Question ID to Delete:");
        if (questionId != null && !questionId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(questionId);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (java.sql.Connection conn = DataBaseConnection.getConnection();
                         java.sql.PreparedStatement stmt = conn.prepareStatement("DELETE FROM Questions WHERE question_id = ?")) {
                        stmt.setInt(1, id);
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Question deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "No question found with the given ID.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Question ID. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Main method to launch the admin dashboard for test case.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Dashboard();
    }
}
