package edu.nwtc.chat.client;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	protected static final String BUNDLE_NAME = "edu.nwtc.chat.client.messages"; //$NON-NLS-1$
	protected static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	protected ResourceBundle bundle;

	public Messages() {
		this(DEFAULT_LOCALE);
	}
	
	public Messages(Locale locale) {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public String getString(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
