package ua.kapitonenko.i18n;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {
	private static final Logger LOGGER = Logger.getLogger(MessageManager.class);
	private static final String BUNDLE_NAME = Application.messageBundle;
	private static MessageManager instance;
	private ResourceBundle resourceBundle;
	
	
	public static MessageManager getInstance(Locale locale) {
		if (instance == null) {
			instance = new MessageManager();
			instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		}
		return instance;
	}
	
	public String getProperty(String key) {
		return (String) resourceBundle.getObject(key);
	}
	
	public String notEmptyMessage(String attribute) {
		return String.format(getProperty(Messages.ERROR_EMPTY), getProperty(attribute));
	}
	
	public String notEqualsMessage(String first, String second) {
		return String.format(getProperty(Messages.ERROR_NOT_EQUALS), getProperty(first), getProperty(second));
	}
	
	public String notUniqueMessage(String attribute) {
		return String.format("%s %s", getProperty(attribute), getProperty(Messages.ERROR_NOT_UNIQUE));
	}
	
	public String registrationSuccess(String next) {
		return String.format("%s %s", getProperty(Messages.SUCCESS_SIGN_UP), getProperty(next));
	}
	
	public String failureMessage(String action) {
		return String.format("%s %s", getProperty(Messages.FAILED_TO), getProperty(action));
	}
	
	
	public String notExists(String attribute) {
		return String.format("%s %s", getProperty(attribute), getProperty(Messages.NOT_EXISTS));
	}
}