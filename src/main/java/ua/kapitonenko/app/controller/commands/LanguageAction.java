package ua.kapitonenko.app.controller.commands;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.persistence.records.LocaleRecord;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Implementation of {@code ActionCommand}.
 * Changes the language of the system.
 */
public class LanguageAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	/**
	 * Validate received language param and changes the language of the system.
	 * Redirects back to previous action.
	 * Only processes POST requests.
	 * Throws {@link MethodNotAllowedException} if request not POST
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		
		String lang = request.getParameter(Keys.LANG);
		List<String> supported = settingsService.getSupportedLanguages();
		
		if (!supported.contains(lang)) {
			logger.warn("Param language:{} Supported languages: {}", lang, supported);
			throw new NotFoundException();
		}
		
		LocaleRecord localeRecord = settingsService.getSupportedLocales().get(lang);
		logger.info("Changing locale from {} to {}", request.getSession().get(Keys.LOCALE), localeRecord.getName());
		request.getSession().set(Keys.LOCALE, localeRecord.getName());
		request.getSession().set(Keys.LOCALE_ID, localeRecord.getId());
		
		return request.goBack();
	}
}
