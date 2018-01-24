package ua.kapitonenko.app.controller.commands.user;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.persistence.records.Cashbox;
import ua.kapitonenko.app.persistence.records.User;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Implementation of {@code ActionCommand}.
 * Prepares login form. Performs validation of user's credentials.
 * Writes valid user and cashbox in session.
 */
public class LoginAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	/**
	 * Returns the URI of login form on GET
	 * or redirects home after successful authentication on POST.
	 */
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
		return request.forward(Pages.USER_FORM, Actions.LOGIN);
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
		
		ValidationBuilder validator = request.getValidator();
		boolean valid = validator
				                .required(username, Keys.USERNAME)
				                .required(password, Keys.PASSWORD)
				                .exists(cashboxId, () -> settingsService.findCashbox(cashboxId) != null, Keys.CASHBOX)
				                .isValid();
		
		if (!valid) {
			logger.warn("Login validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		
		return valid;
	}
	
	private boolean login(RequestWrapper request) {
		User result = userService.findByLoginAndPassword((User) request.getAttribute(Keys.USER));
		
		if (result == null) {
			request.getAlert().addMessage(request.getMessageProvider().getProperty(Keys.ERROR_LOGIN));
			request.getAlert().setMessageType(Keys.ALERT_CLASS_DANGER);
			logger.warn("User not found: {}", request.paramsToString());
			return false;
		}
		
		Cashbox cashbox = settingsService.findCashbox((Long) request.getAttribute(Keys.CASHBOX));
		
		request.getSession().login(result, cashbox);
		logger.info("Logging In: Cashbox={} {}", cashbox.getId(), result);
		return true;
	}
}
