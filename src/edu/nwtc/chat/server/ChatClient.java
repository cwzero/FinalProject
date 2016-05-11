package edu.nwtc.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.LinkedList;

import edu.nwtc.chat.ChatThread;
import edu.nwtc.chat.User;
import edu.nwtc.chat.message.ChatMessage;
import edu.nwtc.chat.message.LoginMessage;
import edu.nwtc.chat.message.LoginResponse;
import edu.nwtc.chat.message.Message;
import edu.nwtc.chat.message.MessageJson;

public class ChatClient extends ChatThread {
	protected ChatClientInput input;
	protected ChatClientOutput output;
	protected ChatChannel channel;

	public ChatClient(InputStream input, OutputStream output, ChatChannel channel) {
		this.input = new ChatClientInput(input);
		this.output = new ChatClientOutput(output);
		this.channel = channel;
	}

	public void sendMessage(Message message) {
		output.pushMessage(message);
	}

	@Override
	public void run() {
		input.start();
		output.start();
	}

	protected class ChatClientInput extends ChatThread {
		protected InputStream input;

		public ChatClientInput(InputStream input) {
			this.input = input;
		}

		@Override
		public void run() {
			InputStreamReader reader = new InputStreamReader(input);
			while (true) {
				MessageJson messageJson = null;
				try {
					messageJson = new MessageJson();
					messageJson.fromJson(reader);
					Message message = messageJson.toMessage();

					if (message instanceof LoginMessage) {
						LoginMessage login = (LoginMessage) message;
						User user = login.getUser();

						try {
							if (Authentication.authenticateUser(user)) {
								output.pushMessage(LoginResponse.success());
							} else {
								output.pushMessage(LoginResponse.failure());
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else if (message instanceof ChatMessage) {
						channel.send(message);
						try {
							Database.createMessage((ChatMessage)message);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				} catch (SocketException ex) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected class ChatClientOutput extends ChatThread {
		protected OutputStream output;
		protected LinkedList<Message> messageQueue;

		public ChatClientOutput(OutputStream output) {
			this.output = output;
			this.messageQueue = new LinkedList<Message>();
		}

		public void pushMessage(Message message) {
			this.messageQueue.push(message);
		}

		@Override
		public void run() {
			OutputStreamWriter writer = new OutputStreamWriter(output);
			while (true) {
				MessageJson messageJson = null;
				int messageIndex = -1;
				if (messageQueue.size() > 0) {
					messageJson = new MessageJson(messageQueue.pop());
				} else {
					Message message = channel.next(messageIndex);
					if (message != null) {
						messageJson = new MessageJson(message);
						messageIndex++;
					}
				}
				try {
					if (messageJson != null) {
						messageJson.toJson(writer);
						writer.flush();
					}
				} catch (SocketException ex) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
