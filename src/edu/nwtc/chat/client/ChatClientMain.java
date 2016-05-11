package edu.nwtc.chat.client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

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

		JFrame chatWindow = new JFrame();
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(600, 600);

		Messages messages = null;
		if (args.length >= 1) {
			if (args[0].equals("help")) {
				System.out.println("Usage: ChatClientMain language [country variant]");
				System.exit(0);
			} else {
				String language = "";
				String country = "";
				String variant = "";
				if (args.length == 1) {
					String loc = args[0];
					if (loc.contains("-")) {
						String[] l = loc.split("-");
						language = l[0];
						country = l[1];
						if (l.length == 3) {
							variant = l[2];
						}
					} else {
						language = loc;
					}
				} else {
					language = args[0];
					country = args[1];
					variant = args[2];
				}
				messages = new Messages(new Locale(language, country, variant));
			}
		} else {
			messages = new Messages();
		}

		@SuppressWarnings("unused")
		Login login = new Login(chatWindow, messages);
	}
}
