package quiz_application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * The {@code Login} class provides a GUI for user authentication.
 * Users can log in as a player or admin, or register for a new account.
 */
public class Login extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    
    /**
     * Constructs a new {@code Login} screen.
     * Users can enter their credentials and log in.
     */
    public Login() {
        setTitle("Quiz Game - Login");
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblUsername.setBounds(94, 174, 794, 80);
        getContentPane().add(lblUsername);
        usernameField = new JTextField();
        usernameField.setBounds(94, 255, 440, 61);
        getContentPane().add(usernameField);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblPassword.setBounds(94, 356, 475, 67);
        getContentPane().add(lblPassword);
        passwordField = new JPasswordField();
        passwordField.setBounds(94, 424, 440, 61);
        getContentPane().add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(null);
        buttonPanel.setBounds(450, 544, 794, 189);
        getContentPane().add(buttonPanel);
        buttonPanel.setLayout(null);
        loginButton = new JButton("Login");
        loginButton.setBounds(248, 5, 165, 46);
        buttonPanel.add(loginButton);
        registerButton = new JButton("Register");
        registerButton.setBounds(248, 84, 165, 46);
        buttonPanel.add(registerButton);
        
        JLabel lblLogIn = new JLabel("LOG IN");
        lblLogIn.setFont(new Font("Tahoma", Font.BOLD, 35));
        lblLogIn.setBounds(697, 27, 794, 80);
        getContentPane().add(lblLogIn);
        registerButton.addActionListener(e -> new SignUp());

        loginButton.addActionListener(e -> authenticateUser());

        setVisible(true);
    }
    
    /**
     * Authenticates the user by checking their credentials in the database.
     * If the username and password are valid, the user is logged in.
     */
    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DataBaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login successful as " + role);
                dispose();
                if (role.equals("Admin")) {
                    new Dashboard();
                } else {
                    new PlayerMenu(username);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to launch the login screen for test case.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Login();
    }
}
