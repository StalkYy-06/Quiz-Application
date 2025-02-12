package quiz_application;

import java.sql.*;

/**
 * The {@code DataBaseConnection} class handles database connectivity.
 */

public class DataBaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/quiz_application";
	private static final String User = "root";
	private static final String Password ="";
	
	/**
     * Establishes a connection to the database.
     * @return A {@code Connection} object or {@code null} if the connection fails.
     */
	
	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, User, Password);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}