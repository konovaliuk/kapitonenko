package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewHelper implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(ViewHelper.class);
	
	private String action;
	private String messageType;
	private List<String> messageList = new ArrayList<>();
	private String link;
	private Map<Long, String> options;
	private Map<String, Object> settingsMap;
	private Model model;
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
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
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public Map<Long, String> getOptions() {
		return options;
	}
	
	public void setOptions(Map<Long, String> options) {
		this.options = options;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Map<String, Object> getSettingsMap() {
		return settingsMap;
	}
	
	public void setSettingsMap(Map<String, Object> settingsMap) {
		this.settingsMap = settingsMap;
	}
	
	public void putSetting(String key, Object value) {
		if (settingsMap == null) {
			settingsMap = new HashMap<>();
		}
		
		settingsMap.put(key, value);
	}
	
	public Object getSetting(String key) {
		if (settingsMap != null) {
			return settingsMap.get(key);
		}
		return null;
	}
}
