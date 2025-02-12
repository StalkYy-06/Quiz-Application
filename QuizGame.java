package quiz_application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * The {@code QuizGame} class represents the quiz interface where players answer multiple choice questions.
 * It displays questions, and handles user interactions, scoring, and saving number of attempts.
 */

public class QuizGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private int currentQuestionIndex = 0;
    private java.util.List<Question> questions;
    private int score = 0;
    private String playerName;
    private String level;
    private int attemptNumber;
    
    /**
     * Constructs a new {@code QuizGame} for a player.
     * 
     * @param playerName   Players name.
     * @param attemptNumber The number of the current attempt (1-5).
     */
    public QuizGame(String playerName, int attemptNumber) {
        getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 19));
        this.playerName = playerName;
        this.attemptNumber = attemptNumber;

        if (attemptNumber == 1) {
            String[] levels = {"Beginner", "Intermediate", "Advanced"};
            level = (String) JOptionPane.showInputDialog(this, "Choose Difficulty Level:",
                    "Select Level", JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
            if (level == null) return;
        }

        setTitle("Quiz Game - Attempt " + attemptNumber);
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JPanel questionPanel = new JPanel();
        questionPanel.setBounds(0, 11, 1592, 33);
        getContentPane().add(questionPanel);
        questionLabel = new JLabel("Loading question...");
        questionLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));
        questionPanel.add(questionLabel);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionsPanel.setBounds(280, 79, 954, 553);
        options = new JRadioButton[4];
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }
        getContentPane().add(optionsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 710, 1592, 33);
        getContentPane().add(buttonPanel);
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
        nextButton.setBounds(658, 643, 256, 33);
        getContentPane().add(nextButton);
        nextButton.addActionListener(e -> nextQuestion());

        loadQuestions();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for this level. Returning to menu.");
            dispose();
            new PlayerMenu(playerName);
            return;
        }

        displayQuestion();
        setVisible(true);
    }
    
    /**
     * Loads random questions from the database based on difficulty selected by the user.
     */
    
    private void loadQuestions() {
        questions = new java.util.ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Questions WHERE level=? ORDER BY RAND() LIMIT 5")) {
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question"),
                        new String[]{rs.getString("option1"), rs.getString("option2"), rs.getString("option3"), rs.getString("option4")},
                        rs.getInt("correct_option")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Displays the question and answer options.
     */
    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText(q.getQuestion());

            for (int i = 0; i < 4; i++) {
                options[i].setText(q.getOptions()[i]);
                options[i].setSelected(false);
            }
        } else {
            endGame();
        }
    }
    
    /**
     * Handles the user's answer selection and moves to the next question.
     * Displays an error message if no option is selected.
     */
    private void nextQuestion() {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i + 1;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer before proceeding!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selected == questions.get(currentQuestionIndex).getCorrectOption()) {
            score++;
        }

        currentQuestionIndex++;
        displayQuestion();
    }

    /**
     * Ends the game,saves the score, and asks the user to continue or return to the menu.
     */
    private void endGame() {
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score);
        saveScore();

        if (attemptNumber < 5) {
            int choice = JOptionPane.showConfirmDialog(this, "Would you like to continue?", "Play Again?", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new QuizGame(playerName, attemptNumber + 1);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new PlayerMenu(playerName);
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "You have completed all 5 attempts. Returning to menu.");
            SwingUtilities.invokeLater(() -> {
                dispose();
                new PlayerMenu(playerName);
            });
        }
    }
    
    /**
     * Saves the player's score into the database.
     */
    
    private void saveScore() {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Scores (name, level, score1, score2, score3, score4, score5, total_score) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE " +
                             "score" + attemptNumber + " = VALUES(score" + attemptNumber + "), " +
                             "total_score = IFNULL(score1, 0) + IFNULL(score2, 0) + IFNULL(score3, 0) + " +
                             "IFNULL(score4, 0) + IFNULL(score5, 0)")) {

            stmt.setString(1, playerName);
            stmt.setString(2, level);

            for (int i = 3; i <= 7; i++) {
                stmt.setObject(i, null);
            }

            stmt.setInt(attemptNumber + 2, score);
            stmt.setInt(8, score);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to start the game for test case.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new QuizGame("Test Player", 1);
    }
}
