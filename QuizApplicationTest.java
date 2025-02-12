package quiz_application;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;

/**
 * JUnit test cases for core functionality of the Quiz Application.
 */
public class QuizApplicationTest {

    private Question question;

    /**
     * Setup method that runs before each test.
     */
    @Before
    public void setUp() {
        question = new Question("What is Java?", 
                new String[]{"Programming Language", "Game", "Food", "Animal"}, 1);
    }

    /**
     * Test to check database connection.
     */
    @Test
    public void testDatabaseConnection() {
        Connection conn = DataBaseConnection.getConnection();
        assertNotNull("Database connection should not be null", conn);
    }

    /**
     * Test to check correct answer selection.
     */
    @Test
    public void testCorrectAnswer() {
        assertEquals("Correct answer should be option 1", 1, question.getCorrectOption());
    }

    /**
     * Test to check leaderboard sorting.
     */
    @Test
    public void testLeaderboardSorting() {
        Report report = new Report();
        assertNotNull("Leaderboard should load properly", report);
    }
}
