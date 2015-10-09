package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
	private static DbManager instance;
	private String url;
	private String username;
	private String password;
	private Connection connection;
	
	private DbManager() {
		super();
		url = "jdbc:mysql://localhost:8889/activityRecog";
		username = "user";
		password = "user";
		System.out.println("-------- MySQL JDBC Connection Testing ------------");

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		connection=null;
		System.out.println("MySQL JDBC Driver Registered!");

		try {
			connection = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("Connected to the database");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	public static DbManager getInstance(){
		if(instance==null){
			instance=new DbManager();
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
	
	
}
