package ua.kapitonenko.Exceptions;

public class DAOException extends CashregisterException {
	public DAOException(Throwable cause) {
		super(cause);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DAOException(String message) {
		super(message);
	}
}
