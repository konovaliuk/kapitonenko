package ua.kapitonenko.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.controller.commands.*;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.exceptions.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
	}
	
	public ActionCommand getCommand(HttpServletRequest request) throws IOException, ServletException {
		String key = request.getRequestURI();
		LOGGER.debug(request.getMethod() + ":" + key);
		ActionCommand command = commands.get(key);
		if (command == null) {
			throw new NotFoundException(key);
		}
		return command;
	}
}
