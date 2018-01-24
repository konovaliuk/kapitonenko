package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

/**
 * {@code ForbiddenException} represents a "Forbidden" HTTP exception with status code 403.
 * Is used when a user is not allowed to perform the requested action.
 */
public class ForbiddenException extends AppException {
	
	/**
	 * Constructor with no arguments calls constructor of superclass with default message.
	 */
	public ForbiddenException() {
		super(Keys.ERROR_FORBIDDEN);
	}
}
