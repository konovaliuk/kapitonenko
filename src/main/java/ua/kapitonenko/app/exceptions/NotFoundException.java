package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

public class NotFoundException extends AppException {
	
	public NotFoundException() {
		super(Keys.ERROR_NOT_FOUND);
	}
}
