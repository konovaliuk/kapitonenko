package ua.kapitonenko.Exceptions;

public class CashregisterException extends RuntimeException {
	public CashregisterException(String message) {
		super(message);
	}
	
	public CashregisterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CashregisterException(Throwable cause) {
		super(cause);
	}
}
