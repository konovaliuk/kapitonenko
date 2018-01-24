package ua.kapitonenko.app.exceptions;

/**
 * The root exception of application. Unchecked.
 * Separates application specific exceptions from platform exceptions.
 */
public class AppException extends RuntimeException {
	
	/**
	 * {@inheritDoc}
	 */
	public AppException(String message) {
		super(message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AppException(Throwable cause) {
		super(cause);
	}
}
