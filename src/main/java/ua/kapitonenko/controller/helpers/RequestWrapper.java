package ua.kapitonenko.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.i18n.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class RequestWrapper {
	private static final Logger LOGGER = Logger.getLogger(RequestWrapper.class);
	
	private HttpServletRequest request;
	private SessionWrapper sessionWrapper;
	private ViewHelper view;
	private MessageManager messageManager;
	
	public RequestWrapper(HttpServletRequest request) {
		this.request = request;
		this.sessionWrapper = new SessionWrapper(request.getSession());
		this.messageManager = MessageManager.getInstance(sessionWrapper.getLocale());
	}
	
	public SessionWrapper getSession() {
		return sessionWrapper;
	}
	
	public ResponseParams forward(String uri) {
		request.setAttribute(Keys.VIEW, view);
		return new ResponseParams(uri, false);
	}
	
	public ResponseParams redirect(String uri) {
		request.setAttribute(Keys.VIEW, view);
		return new ResponseParams(uri, true);
	}
	
	public ResponseParams goBack() {
		String referer = request.getHeader("referer");
		String[] parts = referer.split("/");
		referer = "/" + parts[parts.length - 1];
		LOGGER.debug(referer);
		return new ResponseParams(referer, true);
	}
	
	public boolean isPost() {
		return request.getMethod().equals("POST");
	}
	
	public ViewHelper getView() {
		return view;
	}
	
	public void setView(ViewHelper view) {
		this.view = view;
	}
	
	public String get(String key) {
		return request.getParameter(key);
	}
	
	public MessageManager getMessageManager() {
		return messageManager;
	}
	
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	
	public String getUri() {
		return request.getRequestURI();
	}
}
