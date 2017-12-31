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
		
		session.setAttribute(Application.messageBundle, Application.messageBundle);
		session.setAttribute(Application.settingsBundle, Application.settingsBundle);
		
		if (session.getAttribute(Keys.LOCALE) == null) {
			session.setAttribute(Keys.LOCALE, Application.defaultLocale);
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