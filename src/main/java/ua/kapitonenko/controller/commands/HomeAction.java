package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;

import javax.servlet.ServletException;
import java.io.IOException;

public class HomeAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(HomeAction.class);
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (request.getSession().userIsGuest()) {
			
			return request.redirect(Routes.LOGIN);
		}
		
		return request.redirect(Routes.PRODUCTS);
	}
}
