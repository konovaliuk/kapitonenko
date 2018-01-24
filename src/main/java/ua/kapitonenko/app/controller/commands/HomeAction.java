package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Implementation of {@code ActionCommand}. Processes the default route of controller.
 */
public class HomeAction implements ActionCommand {
	
	/**
	 * Redirects the request to other commands, depending on the user's role.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (request.getSession().userIsGuest()) {
			return request.redirect(Actions.LOGIN);
		}
		
		if (request.getSession().getUser().getUserRoleId().equals(Application.Ids.ROLE_MERCHANDISER.getValue())) {
			return request.redirect(Actions.PRODUCTS);
		}
		
		if (request.getSession().getUser().getUserRoleId().equals(Application.Ids.ROLE_SENIOR.getValue())) {
			return request.redirect(Actions.REPORTS);
		}
		
		return request.redirect(Actions.RECEIPTS);
	}
}
