package ua.kapitonenko.app.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static ua.kapitonenko.app.config.keys.Keys.*;

public class MessageProvider {
	private static final Logger LOGGER = Logger.getLogger(MessageProvider.class);
	
	private static final String BUNDLE_NAME = Application.Params.MESSAGE_BUNDLE.getValue();
	private ResourceBundle resourceBundle;
	private static Map<String, MessageProvider> providers = new HashMap<>();
	private Locale locale;
	
	private MessageProvider() {
	}
	
	public static MessageProvider get(Locale locale) {
		
		if (!providers.containsKey(locale.toString())) {
			MessageProvider instance = new MessageProvider();
			instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
			instance.locale = locale;
			providers.put(locale.toString(), instance);
		}
		
		return providers.get(locale.toString());
	}
	
	public String getProperty(String key) {
		return (String) resourceBundle.getObject(key);
	}
	
	public String notEmptyMessage(String attribute) {
		return String.format(getProperty(ERROR_EMPTY), getProperty(attribute));
	}
	
	public String notEmptyAnyMessage(String attribute) {
		return String.format(getProperty(ERROR_ALL_REQUIRED), getProperty(attribute));
	}
	
	public String notEmptyOneMessage(String first, String second) {
		return String.format(getProperty(ERROR_ONE_EMPTY), getProperty(first), getProperty(second));
	}
	
	public String notEmptyLanguagesMessage(String attribute) {
		return String.format(getProperty(ERROR_EMPTY_LANG), getProperty(attribute));
	}
	
	public String notEqualsMessage(String first, String second) {
		return String.format(getProperty(ERROR_NOT_EQUALS), getProperty(first), getProperty(second));
	}
	
	public String notUniqueMessage(String attribute) {
		return String.format("%s %s", getProperty(attribute), getProperty(ERROR_NOT_UNIQUE));
	}
	
	public String registrationSuccess(String next) {
		return String.format("%s %s", getProperty(SUCCESS_SIGN_UP), getProperty(next));
	}
	
	public String notExists(String attribute) {
		return String.format("%s %s", getProperty(attribute), getProperty(NOT_EXISTS));
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public String decimalValidMessage(String label, int precision) {
		return String.format(getProperty(ERROR_DECIMAL_FORMAT), getProperty(label), precision);
	}
	
	public String concat(String message, String attribute) {
		return String.format(getProperty(message), getProperty(attribute));
	}
}