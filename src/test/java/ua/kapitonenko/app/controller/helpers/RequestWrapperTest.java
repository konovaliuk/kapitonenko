package ua.kapitonenko.app.controller.helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestWrapperTest {
	private static final String URI = "login.jsp";
	private static final String ACTION = "/login";
	
	private RequestWrapper requestWrapper;
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute(Keys.LOCALE)).thenReturn("");
		when(session.getAttribute(Keys.FLASH)).thenReturn(mock(FlashMessage.class));
		
		requestWrapper = new RequestWrapper(request);
		
	}
	
	@Test
	public void constructorShouldInitializeAlertSessionWrapperMessageProvider() throws Exception {
		verify(request).setAttribute(eq(Keys.ALERT), any(AlertContainer.class));
		assertThat(requestWrapper.getSession(), is(notNullValue()));
		assertThat(requestWrapper.getMessageProvider(), is(notNullValue()));
		
	}
	
	@Test
	public void forward() throws Exception {
		ResponseParams expected = new ResponseParams(URI, false);
		
		ResponseParams actual = requestWrapper.forward(URI, ACTION);
		
		assertThat(actual, is(equalTo(expected)));
		verify(request).setAttribute(Keys.ACTION, ACTION);
	}
	
	@Test
	public void redirect() throws Exception {
		ResponseParams expected = new ResponseParams(URI, true);
		
		ResponseParams actual = requestWrapper.redirect(URI);
		
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test
	public void goBackRedirectHomeWhenRefererNull() throws Exception {
		when(request.getHeader(Keys.REFERER)).thenReturn(null);
		
		ResponseParams expected = new ResponseParams(Actions.HOME, true);
		
		ResponseParams actual = requestWrapper.goBack();
		
		assertThat(actual, is(equalTo(expected)));
	}
	
	@Test
	public void goBack() throws Exception {
		when(request.getHeader(Keys.REFERER)).thenReturn("http://cashregister.com/login");
		
		ResponseParams expected = new ResponseParams(Actions.LOGIN, true);
		
		ResponseParams actual = requestWrapper.goBack();
		
		assertThat(actual, is(equalTo(expected)));
	}
	
	
	@Test
	public void goHome() throws Exception {
		ResponseParams expected = new ResponseParams(Actions.HOME, true);
		
		ResponseParams actual = requestWrapper.goHome();
		
		assertThat(actual, is(equalTo(expected)));
	}
	
}