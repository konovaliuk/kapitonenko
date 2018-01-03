package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.entities.Cashbox;
import ua.kapitonenko.domain.entities.Company;
import ua.kapitonenko.domain.entities.User;
import ua.kapitonenko.service.SettingsService;
import ua.kapitonenko.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.kapitonenko.controller.keys.Keys.*;

public class LoginAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class);
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		LOGGER.debug(request.paramsToString());
		
		if (!request.getSession().userIsGuest()) {
			return request.goHome();
		}
		
		request.setAttribute(LINK, Routes.SIGNUP);
		request.setAttribute(CASHBOX, null);
		
		if (request.getAttribute(USER) == null) {
			request.setAttribute(USER, new User());
		}
		
		if (request.isPost()) {
			if (loadAndValidate(request) && login(request)) {
				return request.goHome();
			}
		}
		return request.forward(Pages.SIGNUP, Routes.LOGIN);
	}

	
	private boolean loadAndValidate(RequestWrapper request) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String cashbox = request.getParameter(CASHBOX);
		Long cashboxId = ValidationBuilder.parseId(cashbox);
		
		User user = (User) request.getAttribute(USER);
		user.setUsername(username);
		user.setPassword(password);
		
		request.setAttribute(CASHBOX, cashboxId);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		return validator
				       .required(username, USERNAME)
				       .required(password, PASSWORD)
				       .exists(cashboxId, () -> settingsService.findCashbox(cashboxId) == null, CASHBOX)
				       .isValid();
	}
	
	private boolean login(RequestWrapper request) {
		User result = userService.findByLoginAndPassword((User) request.getAttribute(USER));
		
		if (result == null) {
			request.getAlert().addMessage(request.getMessageManager().getProperty(ERROR_LOGIN));
			request.getAlert().setMessageType(ALERT_CLASS_DANGER);
			return false;
		}
		
		Cashbox cashbox = settingsService.findCashbox((Long) request.getAttribute(CASHBOX));
		Company company = settingsService.findCompany(Application.getId(Application.COMPANY));
		request.getSession().set(COMPANY, company);
		request.getSession().set(CASHBOX, cashbox);
		request.getSession().set(USER, result);
		
		return true;
	}
}
