package edu.nwtc.chat;

public abstract class ChatThread implements Runnable {
	protected Thread thread;
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
}
