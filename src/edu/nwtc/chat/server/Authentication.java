package edu.nwtc.chat.server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import edu.nwtc.chat.User;

public class Authentication {
	protected static Map<String, User> userCache = new HashMap<String, User>();

	public static boolean authenticateUser(User user) throws SQLException {
		User u = getUser(user.getUsername());
		if (u == null) {
			createUser(user);
			return true;
		} else {
			if (user.equals(u)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static User getUser(String username) throws SQLException {
		if (userCache.containsKey(username)) {
			return userCache.get(username);
		} else {
			return queryUser(username);
		}
	}
	
	public static User queryUser(String username) throws SQLException {
		return Database.selectUser(username);
	}
	
	public static User createUser(User user) throws SQLException {
		return Database.createUser(user);
	}
}
