package ua.kapitonenko.app.exceptions;

public class ForbiddenException extends AppException {
	
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
