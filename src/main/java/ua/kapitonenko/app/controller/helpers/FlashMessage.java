package ua.kapitonenko.app.controller.helpers;

public class FlashMessage {
	private String status;
	private String message;
	
	
	public FlashMessage(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
