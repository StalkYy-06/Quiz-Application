package quiz_application;

import java.sql.*;
import java.util.Scanner;

/**
 * The {@code ConsoleReport} class provides a command-line interface for managing quiz reports.
 * It allows users to generate reports, view top performers, generate statistics, and search for competitors.
 */
public class ConsoleReport {
	
	/**
     * The main method provides a menu-driven system for report generation.
     * Users can select options to generate reports, view top performers, or search for competitors.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCompetitor Management System");
            System.out.println("1. Generate Full Report");
            System.out.println("2. Display Top Performer");
            System.out.println("3. Generate Statistics");
            System.out.println("4. Search Competitor by ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    generateFullReport();
                    break;
                case 2:
                    displayTopPerformer();
                    break;
                case 3:
                    generateStatistics();
                    break;
                case 4:
                    searchCompetitorByID(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please select again.");
            }
        }
    }
    
    /**
     * Generates a full report of all competitors sorted by total score.
     * Displays each competitor's ID, name, level, individual scores, and total score.
     */
    private static void generateFullReport() {
        System.out.println("\n=== Full Competitor Report ===");
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Scores ORDER BY total_score DESC, competitor_id ASC")) {
            
            System.out.printf("%-5s %-15s %-12s %-6s %-6s %-6s %-6s %-6s %-10s\n",
                    "ID", "Name", "Level", "S1", "S2", "S3", "S4", "S5", "Total");
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-12s %-6d %-6d %-6d %-6d %-6d %-10d\n",
                        rs.getInt("competitor_id"),
                        rs.getString("name"),
                        rs.getString("level"),
                        rs.getInt("score1"),
                        rs.getInt("score2"),
                        rs.getInt("score3"),
                        rs.getInt("score4"),
                        rs.getInt("score5"),
                        rs.getInt("total_score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Displays the top-performing competitor based on total score.
     * If there are no competitors, it prints a message stating that no data is available.
     */
    private static void displayTopPerformer() {
        System.out.println("\n=== Top Performer ===");
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Scores ORDER BY total_score DESC LIMIT 1")) {
            if (rs.next()) {
                System.out.printf("Top Performer: %s (ID: %d) | Level: %s | Total Score: %d\n",
                        rs.getString("name"),
                        rs.getInt("competitor_id"),
                        rs.getString("level"),
                        rs.getInt("total_score"));
            } else {
                System.out.println("No competitors found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Generates statistics for the quiz competition.
     * Displays the total number of competitors and the average total score.
     */
    private static void generateStatistics() {
        System.out.println("\n=== Quiz Statistics ===");
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total_players, AVG(total_score) AS avg_score FROM Scores")) {
            if (rs.next()) {
                System.out.printf("Total Competitors: %d\n", rs.getInt("total_players"));
                System.out.printf("Average Total Score: %.2f\n", rs.getDouble("avg_score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Searches for a competitor by their ID and displays their details.
     * If the competitor is found, it prints their name, level, and total score.
     * If not found, it displays a message stating that the competitor was not found.
     *
     * @param scanner The Scanner object used to read user input.
     */
    private static void searchCompetitorByID(Scanner scanner) {
        System.out.print("Enter Competitor ID: ");
        int id = scanner.nextInt();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Scores WHERE competitor_id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("Competitor: %s | Level: %s | Total Score: %d\n",
                        rs.getString("name"),
                        rs.getString("level"),
                        rs.getInt("total_score"));
            } else {
                System.out.println("Competitor not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
