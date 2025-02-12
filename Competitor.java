package quiz_application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Competitor} class represents a quiz player.
 * It stores player details and scores.
 */
public class Competitor {
    private int competitorId;
    private String name;
    private String level;
    private int[] scores = new int[5];
    private int totalScore;

    /**
     * Constructs a new {@code Competitor} object.
     * @param id The competitor's unique ID.
     * @param name The competitor's name.
     * @param level The difficulty level they are playing at.
     * @param scores An array of scores from different attempts.
     * @param totalScore The total score from all the 	attempts.
     */
    public Competitor(int id, String name, String level, int[] scores,int totalScore) {
        this.competitorId = id;
        this.name = name;
        this.level = level;
        this.scores = scores;
        this.totalScore=totalScore;
    }
    
    /**
     * Retrieves all competitors from the database, sorted by total score.
     * @return A list of competitors.
     */
    public static List<Competitor> getAllCompetitors() {
        List<Competitor> competitors = new ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
        	     Statement stmt = conn.createStatement()) {
        	     
        	    ResultSet rs = stmt.executeQuery("SELECT * FROM Scores ORDER BY total_score DESC, level, name");

        	    while (rs.next()) {
        	        int[] scores = new int[5];

        	        for (int i = 0; i < 5; i++) {
        	            scores[i] = rs.getInt("score" + (i + 1));
        	            if (rs.wasNull()) {
        	                scores[i] = 0;
        	            }
        	        }
        	        competitors.add(new Competitor(
        	                rs.getInt("competitor_id"),
        	                rs.getString("name"),
        	                rs.getString("level"),
        	                scores,
        	                rs.getInt("total_score")
        	        ));
        	    }
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}

        return competitors;
    }
    /**
     * Returns the total score.
     * @return The total score of the competitor.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Prints the leaderboard.
     */
    public static void printLeaderboard() {
        List<Competitor> competitors = getAllCompetitors();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-12s %-6s %-6s %-6s %-6s %-6s %-6s\n",
                "ID", "Name", "Level", "S1", "S2", "S3", "S4", "S5","Total");
        System.out.println("-----------------------------------------------------------------------------");

        for (Competitor c : competitors) {
            System.out.printf("%-5d %-15s %-12s %-6d %-6d %-6d %-6d %-6d %-6d\n",
                    c.competitorId, c.name, c.level, c.scores[0], c.scores[1], c.scores[2], c.scores[3], c.scores[4], c.getTotalScore());
        }
    }

    public static void main(String[] args) {
        printLeaderboard();
    }
}
