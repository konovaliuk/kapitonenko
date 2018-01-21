package ua.kapitonenko.app.controller.filters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocaleFilterTest {
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private ServletResponse response;
	@Mock
	private FilterChain chain;
	
	
	@Test
	public void doFilterShouldSetDefaultLocale() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute(Keys.LOCALE)).thenReturn(null);
		
		LocaleFilter localeFilter = new LocaleFilter();
		localeFilter.doFilter(request, response, chain);
		
		verify(session).setAttribute(Keys.LOCALE, Application.Params.DEFAULT_LOCALE.getValue());
		verify(session).setAttribute(Keys.LOCALE_ID, Application.Ids.DEFAULT_LOCALE.getValue());
		verify(chain).doFilter(request, response);
	}
	
	@Test
	public void doFilterShouldNotChangeLocale() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute(Keys.LOCALE)).thenReturn("fr_FR");
		
		LocaleFilter localeFilter = new LocaleFilter();
		localeFilter.doFilter(request, response, chain);
		
		verify(session, never()).setAttribute(anyString(), any());
		verify(chain).doFilter(request, response);
		
	}
	
}