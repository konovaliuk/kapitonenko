package ua.kapitonenko.app.controller.helpers;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		FlashMessage that = (FlashMessage) o;
		
		return new EqualsBuilder()
				       .append(status, that.status)
				       .append(message, that.message)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(status)
				       .append(message)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("FlashMessage{")
				       .append("status=").append(status)
				       .append(", message=").append(message)
				       .append("}")
				       .toString();
	}
}
