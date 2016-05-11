package edu.nwtc.chat.server;

import java.util.ArrayList;
import java.util.List;

import edu.nwtc.chat.ChatThread;

public class ChatServer extends ChatThread {
	protected List<ServerConnector> connectors;
	
	public ChatServer() {
		connectors = new ArrayList<ServerConnector>();
	}
	
	public void addConnector(ServerConnector connector) {
		connectors.add(connector);
	}
	
	@Override
	public void run() {
		for (ServerConnector connector: connectors) {
			connector.start();
		}
	}	
}
