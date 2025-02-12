package quiz_application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * The {@code AddQuestion} class provides a form for admins to add or edit quiz questions.
 */

public class AddQuestion extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField questionField, option1Field, option2Field, option3Field, option4Field;
    private JComboBox<String> correctOptionBox, levelBox;
    private JButton saveButton;
    private Dashboard dashboard;
    private int questionId = -1; // -1 means adding new question

   
    /**
	 * @wbp.parser.constructor
	 */
    
    /**
     * Constructs a new {@code AddQuestion} form for adding a question.
     * @param dashboard The admin dashboard reference.
     */
    public AddQuestion(Dashboard dashboard) {
        this.dashboard = dashboard;
        initUI();
    }
    
    /**
     * Constructs a new {@code AddQuestion} form for editing a question.
     * @param dashboard The admin dashboard reference.
     * @param questionId The ID of the question to be edited.
     */
    public AddQuestion(Dashboard dashboard, int questionId) {
        this.dashboard = dashboard;
        this.questionId = questionId;
        initUI();
        loadQuestionDetails();
    }
    
    /**
     * Initializes the user interface for the question form.
     */
    private void initUI() {
        setTitle(questionId == -1 ? "Add Question" : "Edit Question");
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel label = new JLabel("Question:");
        label.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label.setBounds(133, 3, 240, 92);
        getContentPane().add(label);
        questionField = new JTextField();
        questionField.setBounds(698, 23, 611, 53);
        getContentPane().add(questionField);

        JLabel label_1 = new JLabel("Option 1:");
        label_1.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_1.setBounds(133, 86, 302, 92);
        getContentPane().add(label_1);
        option1Field = new JTextField();
        option1Field.setBounds(698, 106, 611, 53);
        getContentPane().add(option1Field);

        JLabel label_2 = new JLabel("Option 2:");
        label_2.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_2.setBounds(133, 167, 336, 92);
        getContentPane().add(label_2);
        option2Field = new JTextField();
        option2Field.setBounds(698, 187, 611, 53);
        getContentPane().add(option2Field);

        JLabel label_3 = new JLabel("Option 3:");
        label_3.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_3.setBounds(133, 259, 230, 92);
        getContentPane().add(label_3);
        option3Field = new JTextField();
        option3Field.setBounds(698, 279, 611, 53);
        getContentPane().add(option3Field);

        JLabel label_4 = new JLabel("Option 4:");
        label_4.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_4.setBounds(133, 351, 369, 92);
        getContentPane().add(label_4);
        option4Field = new JTextField();
        option4Field.setBounds(698, 371, 611, 53);
        getContentPane().add(option4Field);

        JLabel label_5 = new JLabel("Correct Option (1-4):");
        label_5.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_5.setBounds(133, 443, 395, 92);
        getContentPane().add(label_5);
        correctOptionBox = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        correctOptionBox.setBounds(698, 463, 611, 53);
        getContentPane().add(correctOptionBox);

        JLabel label_6 = new JLabel("Difficulty Level:");
        label_6.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_6.setBounds(143, 546, 336, 92);
        getContentPane().add(label_6);
        levelBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        levelBox.setBounds(698, 575, 611, 53);
        getContentPane().add(levelBox);

        saveButton = new JButton(questionId == -1 ? "Add Question" : "Update Question");
        saveButton.setBounds(511, 672, 331, 60);
        saveButton.addActionListener(e -> saveQuestion());
        getContentPane().add(saveButton);

        setVisible(true);
    }
    
    /**
     * Loads question details for editing.
     */
    private void loadQuestionDetails() {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Questions WHERE question_id=?")) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                questionField.setText(rs.getString("question"));
                option1Field.setText(rs.getString("option1"));
                option2Field.setText(rs.getString("option2"));
                option3Field.setText(rs.getString("option3"));
                option4Field.setText(rs.getString("option4"));
                correctOptionBox.setSelectedItem(String.valueOf(rs.getInt("correct_option")));
                levelBox.setSelectedItem(rs.getString("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves the question added into the database.
     */
    private void saveQuestion() {
    	    String question = questionField.getText().trim();
    	    String option1 = option1Field.getText().trim();
    	    String option2 = option2Field.getText().trim();
    	    String option3 = option3Field.getText().trim();
    	    String option4 = option4Field.getText().trim();

    	    if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
    	        JOptionPane.showMessageDialog(this, "All fields must be filled!", "Input Error", JOptionPane.ERROR_MESSAGE);
    	        return;
    	    }
    	    
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(questionId == -1 ?
                     "INSERT INTO Questions (question, option1, option2, option3, option4, correct_option, level) VALUES (?, ?, ?, ?, ?, ?, ?)" :
                     "UPDATE Questions SET question=?, option1=?, option2=?, option3=?, option4=?, correct_option=?, level=? WHERE question_id=?")) {

            stmt.setString(1, questionField.getText());
            stmt.setString(2, option1Field.getText());
            stmt.setString(3, option2Field.getText());
            stmt.setString(4, option3Field.getText());
            stmt.setString(5, option4Field.getText());
            stmt.setInt(6, correctOptionBox.getSelectedIndex() + 1);
            stmt.setString(7, levelBox.getSelectedItem().toString());
            if (questionId != -1) stmt.setInt(8, questionId);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question saved successfully!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
