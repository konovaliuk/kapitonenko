package ua.kapitonenko.app.controller.helpers;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.records.User;

import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SessionWrapper {
	private static final Logger LOGGER = Logger.getLogger(SessionWrapper.class);
	
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
	
	public void logout(){
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
	
	public User getUser(){
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
}
