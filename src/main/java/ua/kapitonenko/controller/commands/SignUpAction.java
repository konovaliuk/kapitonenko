package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.entities.User;
import ua.kapitonenko.domain.entities.UserRole;
import ua.kapitonenko.service.SettingsService;
import ua.kapitonenko.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.kapitonenko.controller.keys.Keys.*;

public class SignUpAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(SignUpAction.class);
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		LOGGER.debug(request.paramsToString());
		
		if (!request.getSession().userIsGuest()) {
			return request.goHome();
		}
		
		request.setAttribute(LINK, Routes.LOGIN);
		request.setAttribute(ROLE_LIST, settingsService.getRoleList());
		
		if (request.getAttribute(USER) == null) {
			request.setAttribute(USER, new User());
		}
		
		if (request.isPost()) {
			if (loadAndValidate(request)) {
				User user = userService.createAccount((User) request.getAttribute(USER));
				
				if (user.isActive()) {
					request.getSession().setFlash(ALERT_CLASS_SUCCESS,
							request.getMessageManager().registrationSuccess(LOGIN_FIRST));
				} else {
					request.getSession().setFlash(ALERT_CLASS_SUCCESS,
							request.getMessageManager().registrationSuccess(CONTACT_ADMIN));
				}
				
				return request.redirect(Routes.LOGIN);
			}
		}
		return request.forward(Pages.SIGNUP, Routes.SIGNUP);
	}
	
	
	private boolean loadAndValidate(RequestWrapper request) {
		
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String confirm = request.getParameter(CONFIRM_PASS);
		String role = request.getParameter(ROLE);
		Long roleId = ValidationBuilder.parseId(role);
		
		User user = (User) request.getAttribute(USER);
		user.setUsername(username);
		user.setUserRoleId(roleId);
		user.setPassword(password);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		return validator
				       .required(username, USERNAME)
				       .required(password, PASSWORD)
				       .required(confirm, CONFIRM_PASS)
				       .identical(password, PASSWORD, confirm, CONFIRM_PASS)
				       .idInList(roleId, (List<UserRole>) request.getAttribute(ROLE_LIST), ROLE)
				       .unique(userService.findByUsername(user), USERNAME)
				       .isValid();
	}
}
