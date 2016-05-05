package edu.nwtc.chat.server;

import java.io.InputStream;
import java.io.OutputStream;

public class ChatClient implements Runnable {
	protected InputStream input;
	protected OutputStream output;

	public ChatClient(InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {

	}
}
