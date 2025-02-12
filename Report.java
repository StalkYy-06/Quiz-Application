package quiz_application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * The {@code Report} class generates the quiz leaderboard.
 * It displays the top scores and player rankings.
 */
public class Report extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable scoreTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructs a new {@code Report} window displaying quiz rankings.
     */
    public Report() {
    	getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 19));
        setTitle("Quiz Leaderboard");
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Rank", "Competitor ID", "Name", "Level", "Score1", "Score2", "Score3", "Score4", "Score5", "Total Score"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Tahoma", Font.PLAIN, 11));

        loadScores(); 
        getContentPane().setLayout(null);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBounds(10, 128, 1582, 615);
        getContentPane().add(scrollPane);
        
        JLabel lblNewLabel = new JLabel("Leaderboard");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 35));
        lblNewLabel.setBounds(649, 32, 301, 70);
        getContentPane().add(lblNewLabel);

        setVisible(true);
    }
    
    /**
     * Fetches and displays player scores sorted by highest total score.
     */
    private void loadScores() {
        tableModel.setRowCount(0); 
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Scores ORDER BY total_score DESC, competitor_id ASC")) {
            int rank = 1; 
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rank,                         
                        rs.getInt("competitor_id"), 
                        rs.getString("name"),
                        rs.getString("level"),
                        rs.getInt("score1"),
                        rs.getInt("score2"),
                        rs.getInt("score3"),
                        rs.getInt("score4"),
                        rs.getInt("score5"),
                        rs.getInt("total_score")
                });
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Main method to test the leaderboard display for test case.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Report();
    }
}
