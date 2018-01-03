package ua.kapitonenko.controller.filters;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(EncodingFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding(Application.getParam(Application.ENCODING));
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void destroy() {
	
	}
}
