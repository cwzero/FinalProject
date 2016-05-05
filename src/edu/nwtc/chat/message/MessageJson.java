package edu.nwtc.chat.message;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import edu.nwtc.chat.User;

public class MessageJson {
	protected String type = "";
	protected String sender = null;
	protected String time = null;
	protected String message = "";
	protected String recipient = null;
	protected User user = null;
	protected String status = "";

	public MessageJson() {

	}

	public MessageJson(Message message) {
		this.type = message.getType();

		if (message instanceof ChatMessage) {
			ChatMessage chat = (ChatMessage) message;
			this.sender = chat.getSender();
			this.time = chat.getTime();
			this.message = chat.getMessage();
		}

		if (message instanceof PrivateMessage) {
			PrivateMessage pm = (PrivateMessage) message;
			this.recipient = pm.getRecipient();
		}

		if (message instanceof LoginMessage) {
			LoginMessage lm = (LoginMessage) message;
			this.user = lm.getUser();
		}

		if (message instanceof LoginResponse) {
			LoginResponse res = (LoginResponse) message;
			this.status = res.getStatus();
		}
	}

	public String toJson() {
		String json = "{\n\t";

		json += createJsonField("type", type);

		if (sender != null) {
			json += ",\n\t" + createJsonField("sender", sender);
		}

		if (time != null) {
			json += ",\n\t" + createJsonField("time", time);
		}

		if (message != null && !message.equals("")) {
			json += ",\n\t" + createJsonField("message", message);
		}

		if (recipient != null) {
			json += ",\n\t" + createJsonField("recipient", recipient);
		}

		if (user != null) {
			json += ",\n\t\"user\" : \n" + user.toJson();
		}

		if (status != null && !status.equals("")) {
			json += ",\n\t" + createJsonField("status", status);
		}

		json += "\n}";
		return json;
	}

	public MessageJson fromJson(Reader reader) throws IOException {
		StreamTokenizer in = new StreamTokenizer(reader);

		in.quoteChar('\'');
		in.quoteChar('\"');
		in.eolIsSignificant(false);
		in.ordinaryChar(':');
		in.ordinaryChar('[');
		in.ordinaryChar(']');
		in.ordinaryChar('{');
		in.ordinaryChar('}');
		in.ordinaryChar(',');

		int level = 0;

		String field = null;

		int next = 0;
		while ((next = in.nextToken()) != StreamTokenizer.TT_EOF) {
			if (next == StreamTokenizer.TT_WORD) {
				System.out.println("WORD: " + in.sval);
				break;
			} else if (next == StreamTokenizer.TT_NUMBER) {
				System.out.println("NUMBER: " + in.nval);
				break;
			} else {
				switch ((char) next) {
				case '"':
					if (field == null) {
						field = in.sval;
					} else if (field.endsWith(".")) {
						field += in.sval;
					} else {
						if (field.equals("type")) {
							type = in.sval;
						} else if (field.equals("sender")) {
							sender = in.sval;
						} else if (field.equals("time")) {
							time = in.sval;
						} else if (field.equals("message")) {
							message = in.sval;
						} else if (field.equals("recipient")) {
							recipient = in.sval;
						} else if (field.equals("status")) {
							status = in.sval;
						} else if (field.equals("user.username")) {
							user.setUsername(in.sval);
						} else if (field.equals("user.password")) {
							user.setUsername(in.sval);
						}

						if (field.contains(".")) {
							field = field.substring(0, field.lastIndexOf('.'));
						} else {
							field = null;
						}
					}
					break;
				case '\'':
					break;
				default:
					if (in.sval == "{") {
						level++;
						field += ".";
						if (field.equals("user.")) {
							user = new User();
						}
					} else if (in.sval == "}") {
						if (level > 0) {
							level--;
							field = null;
						} else {
							return this;
						}
					}
				}
			}
		}
		return this;
	}

	public Message toMessage() {
		Message message = null;

		if (type.equals("CHAT")) {
			message = new ChatMessage(sender, time, this.message);
		} else if (type.equals("LOGIN")) {
			message = new LoginMessage(user);
		} else if (type.equals("RESPONSE")) {
			if (status.equals("SUCCESS")) {
				message = LoginResponse.success();
			} else {
				message = LoginResponse.failure();
			}
		} else if (type.equals("PRIVATE")) {
			message = new PrivateMessage(sender, time, this.message, recipient);
		}

		return message;
	}

	public String createJsonField(String name, String value) {
		return "\"" + name + "\" : \"" + value + "\"";
	}
}
