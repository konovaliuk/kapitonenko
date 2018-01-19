package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.dao.records.LocaleRecord;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.service.SettingsService;

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
		
		String lang = request.getParameter(Keys.LANG);
		List<String> supported = settingsService.getSupportedLanguages();
		
		if (!supported.contains(lang)) {
			throw new NotFoundException(request.getUri());
		}
		
		LocaleRecord localeRecord = settingsService.getSupportedLocales().get(lang);
		request.getSession().set(Keys.LOCALE, localeRecord.getName());
		request.getSession().set(Keys.LOCALE_ID, localeRecord.getId());
		
		return request.goBack();
	}
}
