package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.records.User;
import ua.kapitonenko.app.domain.records.UserRole;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class SignUpAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(SignUpAction.class);
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
							request.getMessageManager().registrationSuccess(Keys.LOGIN_FIRST));
				} else {
					request.getSession().setFlash(Keys.ALERT_CLASS_SUCCESS,
							request.getMessageManager().registrationSuccess(Keys.CONTACT_ADMIN));
				}
				
				return request.redirect(Actions.LOGIN);
			}
		}
		return request.forward(Pages.SIGNUP, Actions.SIGNUP);
	}
	
	
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
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		return validator
				       .required(username, Keys.USERNAME)
				       .required(password, Keys.PASSWORD)
				       .required(confirm, Keys.CONFIRM_PASS)
				       .identical(password, Keys.PASSWORD, confirm, Keys.CONFIRM_PASS)
				       .idInList(roleId, (List<UserRole>) request.getAttribute(Keys.ROLE_LIST), Keys.ROLE)
				       .unique(userService.findByUsername(user), Keys.USERNAME)
				       .isValid();
	}
}