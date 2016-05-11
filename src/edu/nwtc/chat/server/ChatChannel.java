package edu.nwtc.chat.server;

import java.util.LinkedList;
import java.util.List;

import edu.nwtc.chat.message.Message;

public class ChatChannel {
	protected List<Message> messages = new LinkedList<Message>();

	public synchronized void send(Message message) {
		messages.add(message);
	}

	public synchronized Message next(int prev) {
		if (messages.size() <= 0 || messages.size() <= prev + 1) {
			return null;
		}
		return messages.get(prev + 1);
	}
}
