package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.LocaleUtils;
import ua.kapitonenko.controller.keys.Keys;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class SessionHelper {
	public static void setFlash(HttpServletRequest request, String status, String message) {
		FlashMessage flashMessage = new FlashMessage(status, message);
		request.getSession().setAttribute(Keys.FLASH, flashMessage);
	}
	
	public static FlashMessage getFlash(HttpServletRequest request) {
		return (FlashMessage) request.getSession().getAttribute(Keys.FLASH);
	}
	
	public static void removeFlash(HttpServletRequest request) {
		request.getSession().removeAttribute(Keys.FLASH);
	}
	
	public static String getLocaleString(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(Keys.LOCALE);
	}
	
	public static Locale getLocale(HttpServletRequest request) {
		return LocaleUtils.toLocale(getLocaleString(request));
	}
}
