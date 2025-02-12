package quiz_application;

/**
 * The {@code CompetitorList} class provides a way to display the quiz leaderboard.
 * It acts as a helper class to call the leaderboard function from {@code Competitor}.
 */
public class CompetitorList {

    /**
     * Displays the complete quiz leaderboard.
     * It fetches and prints the rankings of all competitors.
     */
    public static void showLeaderboard() {
        Competitor.printLeaderboard();
    }

    /**
     * The main method that runs the leaderboard display.
     * It calls {@code showLeaderboard()} to print the competitor rankings.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        showLeaderboard();
    }
}
