package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

/**
 * {@code MethodNotAllowedException} represents a "Method Not Allowed" HTTP exception with status code 405.
 */
public class MethodNotAllowedException extends AppException {
	
	/**
	 * Constructor with no arguments calls constructor of superclass with default message.
	 */
	public MethodNotAllowedException() {
		super(Keys.ERROR_METHOD);
	}
}
