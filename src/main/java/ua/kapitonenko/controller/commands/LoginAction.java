package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.controller.helpers.ViewHelper;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.User;
import ua.kapitonenko.i18n.MessageManager;
import ua.kapitonenko.i18n.Messages;
import ua.kapitonenko.service.SettingsService;
import ua.kapitonenko.service.UserService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.kapitonenko.i18n.Messages.*;

public class LoginAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class);
	private UserService userService = Application.getServiceFactory().getUserService();
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		init(request);
		
		if (request.isPost()) {
			if (loadAndValidate(request) && login(request)) {
				return request.redirect(Routes.SIGNUP);
			}
		}
		return request.forward(Pages.SIGNUP);
	}
	
	private void init(RequestWrapper request) {
		ViewHelper view = new ViewHelper();
		view.setAction(Routes.LOGIN);
		view.setLink(Routes.SIGNUP);
		view.putSetting(Messages.CASHBOX, null);
		
		User user = new User();
		view.setModel(user);
		request.setView(view);
	}
	
	private boolean loadAndValidate(RequestWrapper request) {
		ViewHelper view = request.getView();
		MessageManager messageManager = request.getMessageManager();
		List<String> errors = view.getMessageList();
		
		String username = request.get(Messages.USERNAME);
		String password = request.get(Messages.PASSWORD);
		String cashbox = request.get(Messages.CASHBOX);
		Long cashboxId = ValidationBuilder.parseId(cashbox);
		
		User user = (User) view.getModel();
		user.setUsername(username);
		user.setPassword(password);
		
		view.putSetting(Messages.CASHBOX, cashboxId);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getView());
		return validator
				       .required(username, USERNAME)
				       .required(password, PASSWORD)
				       .exists(cashboxId, () -> settingsService.findCashbox(cashboxId) == null, CASHBOX)
				       .isValid();
	}
	
	private boolean login(RequestWrapper request) {
		User result = userService.findByLoginAndPassword((User) request.getView().getModel());
		
		if (result == null) {
			request.getView().addMessage(request.getMessageManager().getProperty(ERROR_LOGIN));
			request.getView().setMessageType(Keys.ALERT_CLASS_DANGER);
			return false;
		}
		
		request.getSession().set(Keys.CASHBOX, request.getView().getSetting(CASHBOX));
		request.getSession().login(result);
		return true;
	}
}
