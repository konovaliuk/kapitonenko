package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public class HomeAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(HomeAction.class);
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (request.getSession().userIsGuest()) {
			return request.redirect(Actions.LOGIN);
		}
		
		if (request.getSession().getUser().getUserRoleId().equals(Application.getId(Application.ROLE_MERCHANDISER))) {
			return request.redirect(Actions.PRODUCTS);
		}
		
		if (request.getSession().getUser().getUserRoleId().equals(Application.getId(Application.ROLE_SENIOR))) {
			return request.redirect(Actions.REPORTS);
		}
		
		return request.redirect(Actions.RECEIPTS);
	}
}
