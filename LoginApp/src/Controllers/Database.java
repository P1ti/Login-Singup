package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.User;

public class Database {
	/* MySQL variables for connection */
	private final String url = "jdbc:mysql://localhost:3306/loginapp";
	private final String name = "root";
	private final String pwd = "root";
	Connection conn;
	
	public Connection connect() throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection(url, name, pwd);
			return conn;
		} else {
			System.out.println("Error");
			return null;
		}
	}
	
	/* SIGN UP */
	
	public void createNewEntry(String name, String email, String encryptedPWD) {
		final String query = "INSERT INTO loginapp.users (name, email, password) VALUES (?, ?, ?);";
		try {
			Connection conn = connect();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, encryptedPWD);
			ps.execute();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	
	
	/* LOGIN */
	public List<User> getUser(String email, String encryptedPWD) {
		final String query = "SELECT * FROM loginapp.users WHERE email=?;";
		List<User> userInf = new ArrayList<User>();
		Connection conn;
		User user;
		try {
			conn = connect();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
				userInf.add(user);
			}
			return userInf;
		} catch (SQLException e) {
			return null;
		}
	}
}
