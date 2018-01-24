package ua.kapitonenko.app.exceptions;

/**
 * Wraps exceptions caught during operations on database.
 */
public class DAOException extends AppException {
	
	/**
	 * {@inheritDoc}
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}
