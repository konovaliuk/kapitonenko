package ua.kapitonenko.app.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.keys.Keys;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestWrapper {
	private static final Logger LOGGER = Logger.getLogger(RequestWrapper.class);
	
	private HttpServletRequest request;
	private SessionWrapper sessionWrapper;
	private MessageProvider messageManager;
	private AlertContainer alert;
	
	public RequestWrapper(HttpServletRequest request) {
		this.request = request;
		this.sessionWrapper = new SessionWrapper(request.getSession());
		this.messageManager = MessageProvider.get(sessionWrapper.getLocale());
		initAlert();
	}
	
	public SessionWrapper getSession() {
		return sessionWrapper;
	}
	
	public ResponseParams forward(String uri, String action) {
		request.setAttribute(Keys.ACTION, action);
		return new ResponseParams(uri, false);
	}
	
	public ResponseParams redirect(String uri) {
		return new ResponseParams(uri, true);
	}
	
	public ResponseParams goBack() {
		String referer = request.getHeader("referer");
		
		if (referer == null) {
			return goHome();
		}
		
		String[] parts = referer.split("/");
		referer = String.format("/%s", parts[3]);
		
		LOGGER.debug("referer: " + referer);
		
		return new ResponseParams(referer, true);
	}
	
	public ResponseParams goHome() {
		return new ResponseParams("/", true);
	}
	
	public boolean isPost() {
		return request.getMethod().equals("POST");
	}
	
	public String getParameter(String key) {
		return request.getParameter(key);
	}
	
	public Map<String, String[]> getParams() {
		return request.getParameterMap();
	}
	
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}
	
	public void setAttribute(String key, Object object) {
		request.setAttribute(key, object);
	}
	
	public MessageProvider getMessageManager() {
		return messageManager;
	}
	
	public void setMessageManager(MessageProvider messageManager) {
		this.messageManager = messageManager;
	}
	
	public String getUri() {
		return request.getRequestURI();
	}
	
	public String paramsToString() {
		return request.getParameterMap().entrySet()
				       .stream()
				       .map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
				       .collect(Collectors.joining(", ", "request params: ", ""));
	}
	
	private void initAlert() {
		LOGGER.debug("init alert ");
		if (request.getAttribute(Keys.ALERT) == null) {
			alert = new AlertContainer();
			
			FlashMessage flash = sessionWrapper.getFlash();
			if (flash != null) {
				alert.addMessage(flash.getMessage());
				alert.setMessageType(flash.getStatus());
			}
			
			request.setAttribute(Keys.ALERT, alert);
		}
	}
	
	public AlertContainer getAlert() {
		return (AlertContainer) request.getAttribute(Keys.ALERT);
	}
	
	public String getMethod() {
		return request.getMethod();
	}
}
