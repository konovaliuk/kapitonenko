package ua.kapitonenko.app.controller.commands.user;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class LogoutAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		logger.info("{} logged out", request.getSession().getUser());
		request.getSession().logout();
		return request.goHome();
	}
}
