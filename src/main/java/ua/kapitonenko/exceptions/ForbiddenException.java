package ua.kapitonenko.exceptions;

public class ForbiddenException extends CashregisterException {
	
	public ForbiddenException(String message) {
		super(message);
	}
	
	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ForbiddenException(Throwable cause) {
		super(cause);
	}
}
