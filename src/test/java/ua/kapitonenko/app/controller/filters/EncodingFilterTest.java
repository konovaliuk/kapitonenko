package ua.kapitonenko.app.controller.filters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EncodingFilterTest {
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private ServletResponse response;
	@Mock
	private FilterChain chain;
	
	
	@Test
	public void doFilterShouldSetDefaultLocale() throws Exception {
		
		EncodingFilter encodingFilter = new EncodingFilter();
		encodingFilter.doFilter(request, response, chain);
		
		verify(request).setCharacterEncoding(Application.Params.ENCODING.getValue());
		verify(chain).doFilter(request, response);
	}
	
	
}