package quiz_application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * The {@code SignUp} class provides a GUI for new user registration.
 * It ensures that users do not register with duplicate usernames.
 */
public class SignUp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField, usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    
    /**
     * Constructs a new {@code SignUp} screen for user registration.
     */	
    public SignUp() {
        setTitle("Quiz Game - Sign Up");
        setSize(1606, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblName.setBounds(112, 222, 286, 44);
        getContentPane().add(lblName);
        nameField = new JTextField();
        nameField.setBounds(112, 277, 286, 38);
        getContentPane().add(nameField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 19));
        lblUsername.setBounds(112, 316, 286, 44);
        getContentPane().add(lblUsername);
        usernameField = new JTextField();
        usernameField.setBounds(112, 371, 286, 38);
        getContentPane().add(usernameField);

        JLabel label_2 = new JLabel("Password:");
        label_2.setFont(new Font("Tahoma", Font.PLAIN, 19));
        label_2.setBounds(112, 412, 286, 44);
        getContentPane().add(label_2);
        passwordField = new JPasswordField();
        passwordField.setBounds(112, 454, 286, 38);
        getContentPane().add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(643, 610, 186, 38);
        getContentPane().add(registerButton);
        
        JLabel lblSignUp = new JLabel("SIGN UP");
        lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 40));
        lblSignUp.setBounds(673, 60, 286, 59);
        getContentPane().add(lblSignUp);
        registerButton.addActionListener(e -> registerUser());

        setVisible(true);
    }

    /**
     * Registers a new user by inserting their credentials into the database.
     * Ensures that the username is unique before allowing registration.
     */
    private void registerUser() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Ensure fields are not empty
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DataBaseConnection.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM Users WHERE name=? OR username=?")) {

               checkStmt.setString(1, name);
               checkStmt.setString(2, username);
               ResultSet rs = checkStmt.executeQuery();

               if (rs.next()) {
                   JOptionPane.showMessageDialog(this, "Username or Name already exists. Please choose a different one.", "Registration Error", JOptionPane.ERROR_MESSAGE);
               } else {
                   try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO Users (name, username, password, role) VALUES (?, ?, ?, 'Player')")) {
                       insertStmt.setString(1, name);
                       insertStmt.setString(2, username);
                       insertStmt.setString(3, password);

                       int rowsInserted = insertStmt.executeUpdate();
                       if (rowsInserted > 0) {
                           JOptionPane.showMessageDialog(this, "Player registered successfully!");
                           dispose();
                       } else {
                           JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                       }
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
           }
       }
   }