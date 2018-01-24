package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Model;
import ua.kapitonenko.app.service.Service;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Defines interface for processing requests from user.
 * Is used by the {@link ua.kapitonenko.app.controller.Controller}
 * with the help of {@link ua.kapitonenko.app.controller.helpers.RequestHelper}.
 */
public interface ActionCommand {
	
	/**
	 * Validates request params. Processes the request with the help of {@link Service}
	 * and {@link Model}implementations.
	 * Returns the request destination in the form of {@code ResponseParams}.
	 *
	 * @param request RequestWrapper
	 * @return ResponseParams
	 * @throws ServletException
	 * @throws IOException
	 */
	ResponseParams execute(RequestWrapper request)
			throws ServletException, IOException;
}
