package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

/**
 * {@code NotFoundException} represents a "Not Found" HTTP exception with status code 404.
 */
public class NotFoundException extends AppException {
	
	/**
	 * Constructor with no arguments calls constructor of superclass with default message.
	 */
	public NotFoundException() {
		super(Keys.ERROR_NOT_FOUND);
	}
}
