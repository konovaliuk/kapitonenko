package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.controller.helpers.ViewHelper;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.User;
import ua.kapitonenko.service.SettingsService;
import ua.kapitonenko.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.kapitonenko.controller.keys.Keys.*;

public class SignUpAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(SignUpAction.class);
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		init(request);
		
		if (request.isPost()) {
			if (loadAndValidate(request)) {
				LOGGER.debug("validation success");
				User user = userService.createAccount((User) request.getView().getModel());
				if (user.isActive()) {
					LOGGER.debug("user is active");
					request.getSession().setFlash(ALERT_CLASS_SUCCESS,
							request.getMessageManager().registrationSuccess(LOGIN_FIRST));
				} else {
					request.getSession().setFlash(ALERT_CLASS_SUCCESS,
							request.getMessageManager().registrationSuccess(CONTACT_ADMIN));
				}
				return request.redirect(Routes.LOGIN);
			}
		}
		return request.forward(Pages.SIGNUP);
	}
	
	private void init(RequestWrapper request) {
		ViewHelper view = new ViewHelper();
		view.setAction(Routes.SIGNUP);
		view.setLink(Routes.LOGIN);
		view.setOptions(settingsService.getRolesMap());
		
		User user = new User();
		view.setModel(user);
		request.setView(view);
	}
	
	private boolean loadAndValidate(RequestWrapper request) {
		
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String confirm = request.getParameter(CONFIRM_PASS);
		String role = request.getParameter(ROLE);
		Long roleId = ValidationBuilder.parseId(role);
		
		User user = (User) request.getView().getModel();
		user.setUsername(username);
		user.setUserRoleId(roleId);
		user.setPassword(password);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getView());
		return validator
				       .required(username, USERNAME)
				       .required(password, PASSWORD)
				       .required(confirm, CONFIRM_PASS)
				       .identical(password, PASSWORD, confirm, CONFIRM_PASS)
				       .idInSet(roleId, request.getView().getOptions().keySet(), ROLE)
				       .unique(userService.findByUsername(user), USERNAME)
				       .isValid();
	}
}
