package edu.nwtc.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnector implements Runnable {
	protected ChatServer server;
	protected int port = 9138;

	public ServerConnector() {

	}

	public ServerConnector(int port) {
		this.port = port;
	}

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
				ChatClient client = new ChatClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
				new Thread(client).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
