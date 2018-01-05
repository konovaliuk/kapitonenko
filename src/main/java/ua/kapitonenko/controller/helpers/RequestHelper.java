package ua.kapitonenko.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.commands.*;
import ua.kapitonenko.domain.entities.User;
import ua.kapitonenko.exceptions.ForbiddenException;
import ua.kapitonenko.exceptions.NotFoundException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

public class RequestHelper {
	private static final Logger LOGGER = Logger.getLogger(RequestHelper.class);
	private static RequestHelper instance = new RequestHelper();
	private HashMap<String, ActionCommand> commands = new HashMap<>();
	
	private RequestHelper() {
		initCommands();
	}
	
	public static RequestHelper getInstance() {
		return instance;
	}
	
	private void initCommands() {
		commands.put(Routes.LOGIN, new LoginAction());
		commands.put(Routes.LOGOUT, new LogoutAction());
		commands.put(Routes.HOME, new HomeAction());
		commands.put(Routes.SIGNUP, new SignUpAction());
		commands.put(Routes.LANGUAGE, new LanguageAction());
		commands.put(Routes.PRODUCTS, new ProductListAction());
		commands.put(Routes.PRODUCTS_CREATE, new ProductCreateAction());
		commands.put(Routes.PRODUCTS_ADD, new ProductAddAction());
		commands.put(Routes.PRODUCTS_DELETE, new ProductDeleteAction());
		commands.put(Routes.RECEIPT_CREATE, new ReceiptCreateAction());
		commands.put(Routes.RECEIPT_RETURN, new ReceiptReturnAction());
		commands.put(Routes.RECEIPT_CANCEL, new ReceiptCancelAction());
		commands.put(Routes.RECEIPTS, new ReceiptListAction());
	}
	
	public ActionCommand getCommand(RequestWrapper request) throws IOException, ServletException {
		String key = request.getUri();
		LOGGER.debug(request.getMethod() + ": " + key);
		ActionCommand command = commands.get(key);
		
		if (command == null) {
			LOGGER.debug("command not found");
			throw new NotFoundException(key);
		}
		User user = request.getSession().getUser();
		
		if (user == null && !Application.guestAllowed(key)) {
			
			return commands.get(Routes.HOME);
			
		}
		
		if (user != null && !Application.allowed(user.getUserRoleId(), key)) {
			
			throw new ForbiddenException(key);
		}
		
		return command;
	}
	
}
