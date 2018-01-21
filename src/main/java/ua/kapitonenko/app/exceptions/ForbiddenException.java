package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

public class ForbiddenException extends AppException {
	
	public ForbiddenException() {
		super(Keys.ERROR_FORBIDDEN);
	}
}
