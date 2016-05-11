package edu.nwtc.chat.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.nwtc.chat.User;
import edu.nwtc.chat.message.ChatMessage;

public class Database {
	protected static Connection connection = null;

	protected static String CREATE_USER_TABLE = "CREATE TABLE ACCOUNT (USER_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, USERNAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL, PRIMARY KEY (USER_ID))";
	protected static String CREATE_MESSAGE_TABLE = "CREATE TABLE MESSAGE (MESSAGE_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, SENDER_ID INT NOT NULL, DATE VARCHAR(50) NOT NULL, MESSAGE_TEXT VARCHAR(255) NOT NULL, PRIMARY KEY (MESSAGE_ID), CONSTRAINT SENDER_ID_REF FOREIGN KEY (SENDER_ID) REFERENCES ACCOUNT(USER_ID))";

	protected static String CREATE_USER = "INSERT INTO ACCOUNT (USERNAME, PASSWORD) VALUES (?, ?)";
	protected static String SELECT_USER = "SELECT * FROM ACCOUNT WHERE USERNAME = ?";
	
	protected static String CREATE_MESSAGE = "INSERT INTO MESSAGE (SENDER_ID, DATE, MESSAGE_TEXT) VALUES (?, ?, ?)";

	public static void init()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		connection = DriverManager.getConnection("jdbc:derby:chat;create=true");
		File firstRun = new File("run");
		if (!firstRun.exists()) {
			createTables();
			try {
				firstRun.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createTables() throws SQLException {
		createUserTable();
		createMessageTable();
	}

	public static void createUserTable() throws SQLException {
		connection.createStatement().execute(CREATE_USER_TABLE);
	}

	public static User selectUser(String username) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SELECT_USER);
		statement.setString(1, username);
		statement.execute();

		if (statement.getResultSet().next()) {
			User user = new User(statement.getResultSet().getInt(1), statement.getResultSet().getString(2),
					statement.getResultSet().getString(3).toCharArray());
			return user;
		} else {
			return null;
		}
	}

	public static User createUser(User user) throws SQLException {
		if (user == null) {
			return null;
		}
		
		User selected = selectUser(user.getUsername());
		if (selected != null) {
			return selected;
		}

		PreparedStatement statement = connection.prepareStatement(CREATE_USER);
		statement.setString(1, user.getUsername());
		if (user.getPassword() == null) {
			System.out.println("Password is null for user " + user.getUsername());
		}
		statement.setString(2, new String(user.getPassword()));
		statement.execute();
		return selectUser(user.getUsername());
	}
	
	public static void createMessage(ChatMessage message) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_MESSAGE);
		User user = selectUser(message.getSender());
		statement.setInt(1, user.getId());
		statement.setString(2, message.getTime());
		statement.setString(3, message.getMessage());
		statement.execute();
	}

	public static void createMessageTable() throws SQLException {
		connection.createStatement().execute(CREATE_MESSAGE_TABLE);
	}

	public static void close() throws SQLException {
		connection.close();
		DriverManager.getConnection("jdbc:derby:;shutdown=true");
	}
}
