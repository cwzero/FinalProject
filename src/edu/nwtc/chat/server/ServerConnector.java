package edu.nwtc.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.nwtc.chat.ChatThread;

public class ServerConnector extends ChatThread {
	protected ChatServer server;
	protected int port = 9138;
	protected ChatChannel channel;

	public ServerConnector(ChatChannel channel) {
		this(9138, channel);
	}

	public ServerConnector(int port, ChatChannel channel) {
		this.port = port;
		this.channel = channel;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			Socket clientSocket = null;

			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				ChatClient client = new ChatClient(clientSocket.getInputStream(), clientSocket.getOutputStream(), channel);
				client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
