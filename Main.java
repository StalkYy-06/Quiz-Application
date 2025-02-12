package quiz_application;

import javax.swing.*;

/**
 * The {@code Main} class serves as the entry point for the quiz application.
 * It initializes the application by launching the login screen.
 */
public class Main {
	
	 /**
     * The main method starts the quiz application.
     * It uses {@code SwingUtilities.invokeLater} to ensure that the GUI is created on the Event Dispatch Thread (EDT).
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
