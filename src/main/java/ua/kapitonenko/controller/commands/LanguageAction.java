package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.Exceptions.MethodNotAllowedException;
import ua.kapitonenko.Exceptions.NotFoundException;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class LanguageAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(LanguageAction.class);
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		String lang = request.get("l");
		LOGGER.debug(lang);
		Map<String, String> langLocale = settingsService.getLocaleMap();
		Set<String> supportedLang = langLocale.keySet();
		LOGGER.debug(supportedLang.toString());
		
		
		if (!supportedLang.contains(lang)) {
			throw new NotFoundException(request.getUri());
		}
		
		request.getSession().set(Keys.LOCALE, langLocale.get(lang));
		
		return request.goBack();
	}
}
