package ua.kapitonenko.controller.helpers;

import org.apache.log4j.Logger;
import ua.kapitonenko.Exceptions.NotFoundException;
import ua.kapitonenko.controller.commands.*;
import ua.kapitonenko.controller.keys.Routes;

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
		commands.put(Routes.HOME, new HomeAction());
		commands.put(Routes.SIGNUP, new SignUpAction());
		commands.put(Routes.LANGUAGE, new LanguageAction());
		
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
