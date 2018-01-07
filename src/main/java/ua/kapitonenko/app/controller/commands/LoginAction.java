package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.records.Cashbox;
import ua.kapitonenko.app.domain.records.Company;
import ua.kapitonenko.app.domain.records.User;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;

public class LoginAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class);
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		if (!request.getSession().userIsGuest()) {
			return request.goHome();
		}
		
		request.setAttribute(Keys.LINK, Actions.SIGNUP);
		request.setAttribute(Keys.CASHBOX, null);
		
		if (request.getAttribute(Keys.USER) == null) {
			request.setAttribute(Keys.USER, new User());
		}
		
		if (request.isPost()) {
			if (loadAndValidate(request) && login(request)) {
				return request.goHome();
			}
		}
		return request.forward(Pages.SIGNUP, Actions.LOGIN);
	}

	
	private boolean loadAndValidate(RequestWrapper request) {
		String username = request.getParameter(Keys.USERNAME);
		String password = request.getParameter(Keys.PASSWORD);
		String cashbox = request.getParameter(Keys.CASHBOX);
		Long cashboxId = ValidationBuilder.parseId(cashbox);
		
		User user = (User) request.getAttribute(Keys.USER);
		user.setUsername(username);
		user.setPassword(password);
		
		request.setAttribute(Keys.CASHBOX, cashboxId);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		return validator
				       .required(username, Keys.USERNAME)
				       .required(password, Keys.PASSWORD)
				       .exists(cashboxId, () -> settingsService.findCashbox(cashboxId) == null, Keys.CASHBOX)
				       .isValid();
	}
	
	private boolean login(RequestWrapper request) {
		User result = userService.findByLoginAndPassword((User) request.getAttribute(Keys.USER));
		
		if (result == null) {
			request.getAlert().addMessage(request.getMessageManager().getProperty(Keys.ERROR_LOGIN));
			request.getAlert().setMessageType(Keys.ALERT_CLASS_DANGER);
			return false;
		}
		
		Cashbox cashbox = settingsService.findCashbox((Long) request.getAttribute(Keys.CASHBOX));
		Company company = settingsService.findCompany(Application.getId(Application.COMPANY));
		request.getSession().set(Keys.COMPANY, company);
		request.getSession().set(Keys.CASHBOX, cashbox);
		request.getSession().set(Keys.USER, result);
		
		return true;
	}
}
