package edu.nwtc.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.RuntimeErrorException;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.nwtc.chat.ChatThread;
import edu.nwtc.chat.User;
import edu.nwtc.chat.message.ChatMessage;
import edu.nwtc.chat.message.LoginMessage;
import edu.nwtc.chat.message.LoginResponse;
import edu.nwtc.chat.message.Message;
import edu.nwtc.chat.message.MessageJson;

public class ChatClient {
	protected static DateFormat FORMAT = new SimpleDateFormat();
	protected String username;
	protected char[] password;

	protected JTextArea messageArea;
	protected JTextField contactArea;

	protected ChatClientInput input;
	protected ChatClientOutput output;

	public ChatClient(String username, char[] password, String server) {
		this(username, password, server, 9138);
	}

	public ChatClient(String username, char[] password, String server, int port) {
		this.username = username;
		this.password = password;
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket(server, port);
			output = new ChatClientOutput(socket.getOutputStream());
			input = new ChatClientInput(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		LoginMessage message = new LoginMessage(new User(username, password));
		output.sendMessage(message);
		input.start();
	}

	public void sendMessage(String message) {
		ChatMessage msg = new ChatMessage(username, FORMAT.format(new Date()), message);
		output.sendMessage(msg);
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}

	public void setContactArea(JTextField contactArea) {
		this.contactArea = contactArea;
	}

	protected class ChatClientInput extends ChatThread {
		protected Reader reader;

		public ChatClientInput(InputStream input) {
			this.reader = new InputStreamReader(input);
		}

		@Override
		public void run() {
			MessageJson json = new MessageJson();
			try {
				json.fromJson(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Message message = json.toMessage();

			if (message instanceof LoginResponse) {
				if (!message.equals(LoginResponse.success())) {
					throw new RuntimeErrorException(new Error("Incorrect password"));
				}
			}

			while (true) {
				try {
					json.fromJson(reader);
				} catch (IOException e) {
					e.printStackTrace();
				}
				message = json.toMessage();

				handleMessage(message);
			}
		}

		public void handleMessage(Message message) {
			if (message instanceof ChatMessage) {
				ChatMessage msg = (ChatMessage) message;
				if (!msg.getSender().equals(username)) {
					messageArea.append(msg.getSender() + ": " + msg.getMessage() + "\n");
				}
			}
		}
	}

	protected class ChatClientOutput {
		protected Writer writer;

		public ChatClientOutput(OutputStream output) {
			writer = new OutputStreamWriter(output);
		}

		public void sendMessage(Message message) {
			MessageJson json = new MessageJson(message);
			try {
				json.toJson(writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
