package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.exceptions.MethodNotAllowedException;
import ua.kapitonenko.exceptions.NotFoundException;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;


public class LanguageAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(LanguageAction.class);
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		String lang = request.getParameter("l");
		LOGGER.debug(lang);
		List<String> supported = settingsService.getSupportedLanguages();
		
		if (!supported.contains(lang)) {
			throw new NotFoundException(request.getUri());
		}
		LOGGER.debug(settingsService.getSupportedLocales());

		request.getSession().set(Keys.LOCALE, settingsService.getSupportedLocales().get(lang).getName());
		request.getSession().set(Keys.LOCALE_ID, settingsService.getSupportedLocales().get(lang).getId());
		
		return request.goBack();
	}
}
