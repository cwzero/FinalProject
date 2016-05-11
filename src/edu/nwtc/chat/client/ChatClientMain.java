package edu.nwtc.chat.client;

import javax.swing.JFrame;

public class ChatClientMain {
	public static void main(String[] args) {
		JFrame chatWindow  = new JFrame();
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(600, 600);
		@SuppressWarnings("unused")
		Login login = new Login(chatWindow);
	}
}
