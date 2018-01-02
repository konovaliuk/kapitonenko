package ua.kapitonenko.controller.filters;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocaleFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(LocaleFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		
		session.setAttribute(Application.MESSAGE_BUNDLE, Application.MESSAGE_BUNDLE);
		session.setAttribute(Application.SETTINGS_BUNDLE, Application.SETTINGS_BUNDLE);
		
		if (session.getAttribute(Keys.LOCALE) == null) {
			session.setAttribute(Keys.LOCALE, Application.DEFAULT_LOCALE);
			session.setAttribute(Keys.LOCALE_ID, Application.getId(Application.DEFAULT_LOCALE));
		}
		
		if (session.getAttribute(Keys.LANGUAGES) == null) {
			SettingsService settingsService = Application.getServiceFactory().getSettingsService();
			session.setAttribute(Keys.LANGUAGES, settingsService.getSupportedLanguages());
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void destroy() {
	
	}
}