package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.exceptions.MethodNotAllowedException;

import javax.servlet.ServletException;
import java.io.IOException;

public class LogoutAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(LogoutAction.class);
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		request.getSession().logout();
		return request.goHome();
	}
}
