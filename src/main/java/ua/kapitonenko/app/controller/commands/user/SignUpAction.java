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
import ua.kapitonenko.app.dao.records.User;
import ua.kapitonenko.app.dao.records.UserRole;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class SignUpAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		if (!request.getSession().userIsGuest()) {
			return request.goHome();
		}
		
		request.setAttribute(Keys.LINK, Actions.LOGIN);
		request.setAttribute(Keys.ROLE_LIST, settingsService.getRoleList());
		
		if (request.getAttribute(Keys.USER) == null) {
			request.setAttribute(Keys.USER, new User());
		}
		
		if (request.isPost()) {
			if (loadAndValidate(request)) {
				User user = userService.createAccount((User) request.getAttribute(Keys.USER));
				
				if (user.isActive()) {
					request.getSession().setFlash(Keys.ALERT_CLASS_SUCCESS,
							request.getMessageProvider().registrationSuccess(Keys.LOGIN_FIRST));
				} else {
					request.getSession().setFlash(Keys.ALERT_CLASS_SUCCESS,
							request.getMessageProvider().registrationSuccess(Keys.CONTACT_ADMIN));
				}
				logger.info("New user created: {}", user);
				return request.redirect(Actions.LOGIN);
			}
		}
		return request.forward(Pages.USER_FORM, Actions.SIGNUP);
	}
	
	@SuppressWarnings("unchecked")
	private boolean loadAndValidate(RequestWrapper request) {
		
		String username = request.getParameter(Keys.USERNAME);
		String password = request.getParameter(Keys.PASSWORD);
		String confirm = request.getParameter(Keys.CONFIRM_PASS);
		String role = request.getParameter(Keys.ROLE);
		Long roleId = ValidationBuilder.parseId(role);
		
		User user = (User) request.getAttribute(Keys.USER);
		user.setUsername(username);
		user.setUserRoleId(roleId);
		user.setPassword(password);
		
		ValidationBuilder validator = request.getValidator();
		boolean valid = validator
				       .required(username, Keys.USERNAME)
				       .required(password, Keys.PASSWORD)
				       .required(confirm, Keys.CONFIRM_PASS)
				       .identical(password, Keys.PASSWORD, confirm, Keys.CONFIRM_PASS)
				       .idInList(roleId, (List<UserRole>) request.getAttribute(Keys.ROLE_LIST), Keys.ROLE)
				       .unique(userService.findByUsername(user), Keys.USERNAME)
				       .isValid();
		
		if (!valid) {
			logger.warn("SignUp validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		return valid;
	}
}
