package ua.kapitonenko.app.controller.filters;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocaleFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		
		if (session.getAttribute(Keys.LOCALE) == null) {
			session.setAttribute(Keys.LOCALE, Application.Params.DEFAULT_LOCALE.getValue());
			session.setAttribute(Keys.LOCALE_ID, Application.Ids.DEFAULT_LOCALE.getValue());
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