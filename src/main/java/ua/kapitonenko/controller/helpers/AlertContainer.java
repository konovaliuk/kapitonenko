package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.config.keys.Keys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlertContainer implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(AlertContainer.class);
	
	private String messageType;
	private List<String> messageList = new ArrayList<>();
	
	
	public String getMessageType() {
		if (StringUtils.isEmpty(messageType)) {
			return Keys.ALERT_CLASS_INFO;
		}
		return messageType;
	}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public List<String> getMessageList() {
		return messageList;
	}
	
	public void addMessage(String message) {
		this.messageList.add(message);
	}
	
	public boolean isHasMessages() {
		return !this.messageList.isEmpty();
	}
}
