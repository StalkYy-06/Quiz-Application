package quiz_application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * The {@code PlayerMenu} class represents the main menu for players.
 * It allows players to start a quiz, check high scores, view their details, or log out.
 */
public class PlayerMenu extends JFrame {
    private static final long serialVersionUID = 1L;
    private String playerName;
    
    /**
     * Constructs a new {@code PlayerMenu} for the given player.
     *
     * @param playerName The name of the logged-in player.
     */
    public PlayerMenu(String playerName) {
        this.playerName = playerName;

        setTitle("Player Menu - " + playerName);
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton playButton = new JButton("Play Quiz");
        playButton.setBounds(140, 251, 363, 60);
        JButton highScoreButton = new JButton("Check High Scores");
        highScoreButton.setBounds(1047, 251, 363, 60);
        JButton detailsButton = new JButton("View Player Details");
        detailsButton.setBounds(140, 412, 363, 60);
        JButton consoleReportButton = new JButton("Console Report");
        consoleReportButton.setBounds(1047, 412, 363, 60);
        JButton logoutButton = new JButton("Exit / Logout");
        logoutButton.setBounds(593, 578, 363, 60);
        getContentPane().setLayout(null);

        getContentPane().add(playButton);
        getContentPane().add(highScoreButton);
        getContentPane().add(detailsButton);
        getContentPane().add(consoleReportButton);
        getContentPane().add(logoutButton);
        
        JLabel lblNewLabel = new JLabel("Quiz App");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 35));
        lblNewLabel.setBounds(682, 69, 274, 60);
        getContentPane().add(lblNewLabel);

        playButton.addActionListener(e -> {
            dispose();
            new QuizGame(playerName, 1);
        });

        highScoreButton.addActionListener(e -> {
            new Report();
        });

        detailsButton.addActionListener(e -> {
            showPlayerDetails();
        });
        
        consoleReportButton.addActionListener(e -> {
            new Thread(() -> ConsoleReport.main(new String[]{})).start();
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            dispose();
            new Login();
        });

        setVisible(true);
    }
    
    
    
    /**
     * Fetches and displays the player's details from the database.
     */
    private void showPlayerDetails() {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Scores WHERE name=?")) {
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String details = "Player: " + playerName +
                        "\nLevel: " + rs.getString("level") +
                        "\nScore 1: " + rs.getInt("score1") +
                        "\nScore 2: " + rs.getInt("score2") +
                        "\nScore 3: " + rs.getInt("score3") +
                        "\nScore 4: " + rs.getInt("score4") +
                        "\nScore 5: " + rs.getInt("score5");
                JOptionPane.showMessageDialog(this, details, "Player Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No records found for " + playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method for testing the {@code PlayerMenu}.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new PlayerMenu("Test Player");
    }
}
