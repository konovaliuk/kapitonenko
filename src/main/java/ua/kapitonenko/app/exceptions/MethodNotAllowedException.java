package ua.kapitonenko.app.exceptions;

import ua.kapitonenko.app.config.keys.Keys;

public class MethodNotAllowedException extends AppException {
	
	public MethodNotAllowedException() {
		super(Keys.ERROR_METHOD);
	}
}
