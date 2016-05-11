package edu.nwtc.chat.server;

import java.sql.SQLException;

public class ChatServerMain {
	public static void main(String[] args) {
		try {
			Database.init();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ChatServer server = new ChatServer();
		ChatChannel channel = new ChatChannel();
		ServerConnector connector = new ServerConnector(channel);
		server.addConnector(connector);
		server.start();
	}
}
