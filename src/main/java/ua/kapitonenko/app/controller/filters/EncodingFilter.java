package ua.kapitonenko.app.controller.filters;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;

import javax.servlet.*;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class EncodingFilter implements Filter {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		request.setCharacterEncoding(Application.Params.ENCODING.getValue());
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	@Override
	public void destroy() {
	
	}
}
