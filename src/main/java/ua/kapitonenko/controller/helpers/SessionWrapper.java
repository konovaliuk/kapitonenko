package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.LocaleUtils;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SessionWrapper {
	private HttpSession session;
	private ViewHelper view;
	
	public SessionWrapper(HttpSession session) {
		this.session = session;
	}
	
	public void setFlash(String status, String message) {
		FlashMessage flashMessage = new FlashMessage(status, message);
		session.setAttribute(Keys.FLASH, flashMessage);
	}
	
	public FlashMessage getFlash() {
		return (FlashMessage) session.getAttribute(Keys.FLASH);
	}
	
	public void removeFlash() {
		session.removeAttribute(Keys.FLASH);
	}
	
	public String getLocaleString() {
		return (String) session.getAttribute(Keys.LOCALE);
	}
	
	public Locale getLocale() {
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
	
	public void set(String name, Object value) {
		session.setAttribute(name, value);
	}
	
	public Object get(String key) {
		return session.getAttribute(key);
	}
}
