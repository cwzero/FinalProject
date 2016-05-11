package edu.nwtc.chat.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

public class ChatClientMain {
	public static void main(String[] args) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("BuxtonSketch.ttf")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame chatWindow  = new JFrame();
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(600, 600);
		
		Messages messages = new Messages();
		
		@SuppressWarnings("unused")
		Login login = new Login(chatWindow, messages);
	}
}
