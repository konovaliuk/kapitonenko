package ua.kapitonenko.app.controller.helpers;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.records.Cashbox;
import ua.kapitonenko.app.dao.records.User;

import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class SessionWrapper {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private HttpSession session;
	
	
	public SessionWrapper(HttpSession session) {
		this.session = session;
	}
	
	public void setFlash(String status, String message) {
		FlashMessage flashMessage = new FlashMessage(status, message);
		session.setAttribute(Keys.FLASH, flashMessage);
	}
	
	public FlashMessage getFlash() {
		FlashMessage flash = (FlashMessage) session.getAttribute(Keys.FLASH);
		removeFlash();
		return flash;
	}
	
	public void removeFlash() {
		session.removeAttribute(Keys.FLASH);
	}
	
	public String getLocaleString() {
		return (String) session.getAttribute(Keys.LOCALE);
	}
	
	public Long getLocaleId() {
		return (Long) session.getAttribute(Keys.LOCALE_ID);
	}
	
	public Locale getLocale() {
		return LocaleUtils.toLocale(getLocaleString());
	}
	
	public void logout() {
		session.invalidate();
	}
	
	public void set(String name, Object value) {
		session.setAttribute(name, value);
	}
	
	public Object get(String key) {
		return session.getAttribute(key);
	}
	
	public boolean userIsGuest() {
		return session.getAttribute(Keys.USER) == null;
	}
	
	public User getUser() {
		return (User) session.getAttribute(Keys.USER);
	}
	
	public Long getUserId() {
		if (getUser() == null) {
			return null;
		}
		
		return getUser().getId();
	}
	
	public void remove(String key) {
		session.removeAttribute(key);
	}
	
	public void login(User user, Cashbox cashbox) {
		session.setAttribute(Keys.CASHBOX, cashbox);
		session.setAttribute(Keys.USER, user);
	}
	
	private String attributesToString() {
		return Collections.list(session.getAttributeNames())
				       .stream()
				       .map(n -> "        " + n + "=" + session.getAttribute(n))
				       .collect(Collectors.joining(lineSeparator(), "Session:" + lineSeparator(), ""));
	}
	
	@Override
	public String toString() {
		return String.format("%s", attributesToString());
	}
}
