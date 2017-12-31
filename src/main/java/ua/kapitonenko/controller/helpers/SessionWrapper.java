package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SessionWrapper {
	private static final Logger LOGGER = Logger.getLogger(SessionWrapper.class);
	
	private HttpSession session;
	private ViewHelper view;

	
	public SessionWrapper(HttpSession session) {
		this.session = session;
	}
	
	public void setFlash(String status, String message) {
		LOGGER.debug("set flash start");
		FlashMessage flashMessage = new FlashMessage(status, message);
		session.setAttribute(Keys.FLASH, flashMessage);
		LOGGER.debug(session.getAttribute(Keys.FLASH));
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
	
	public Locale getLocale() {
		LOGGER.debug(LocaleUtils.toLocale(getLocaleString()).getDisplayLanguage(LocaleUtils.toLocale(getLocaleString())));
		return LocaleUtils.toLocale(getLocaleString());
	}
	
	public ViewHelper getView() {
		return view;
	}
	
	public void setView(ViewHelper view) {
		this.view = view;
		
		FlashMessage flashMessage = getFlash();
		if (flashMessage != null) {
			view.setMessageType(flashMessage.getStatus());
			view.addMessage(flashMessage.getMessage());
			removeFlash();
		}
	}
	
	public void login(User user) {
		session.setAttribute(Keys.USER, user);
	}
	
	public void logout(){
		// TODO invalidate session
		//session.invalidate();
		session.removeAttribute(Keys.USER);
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

}
