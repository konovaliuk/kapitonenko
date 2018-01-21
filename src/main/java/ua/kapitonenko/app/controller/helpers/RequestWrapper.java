package ua.kapitonenko.app.controller.helpers;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class RequestWrapper {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private HttpServletRequest request;
	private SessionWrapper sessionWrapper;
	private MessageProvider messageProvider;
	
	public RequestWrapper(HttpServletRequest request) {
		this.request = request;
		sessionWrapper = new SessionWrapper(request.getSession());
		messageProvider = MessageProvider.get(sessionWrapper.getLocale());
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
		String referer = request.getHeader(Keys.REFERER);
		
		if (referer == null) {
			return goHome();
		}
		
		String[] parts = referer.split("/");
		referer = String.format("/%s", parts[3]);
		
		return new ResponseParams(referer, true);
	}
	
	public ResponseParams goHome() {
		return new ResponseParams(Actions.HOME, true);
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
	
	public MessageProvider getMessageProvider() {
		return messageProvider;
	}
	
	public String getUri() {
		return request.getRequestURI();
	}
	
	public String getUrl() {
		return request.getRequestURL().toString();
	}
	
	private void initAlert() {
		if (request.getAttribute(Keys.ALERT) == null) {
			AlertContainer alert = new AlertContainer();
			
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
	
	public ValidationBuilder getValidator() {
		return new ValidationBuilder(messageProvider, getAlert());
	}
	
	public String headersToString() {
		return Collections.list(request.getHeaderNames())
				       .stream()
				       .map(n -> "        " + n + "=" + request.getHeader(n))
				       .collect(Collectors.joining(lineSeparator(), "Headers:" + lineSeparator(), ""));
	}
	
	public String paramsToString() {
		return request.getParameterMap().entrySet()
				       .stream()
				       .map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
				       .collect(Collectors.joining(", ", "Params: ", ""));
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s%n    %s%n    Remote address: %s%n    %s%n    %s",
				getMethod(), request.getRequestURL(), paramsToString(), request.getRemoteAddr(), headersToString(), sessionWrapper);
	}

}
