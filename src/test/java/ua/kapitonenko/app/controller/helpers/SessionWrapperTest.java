package ua.kapitonenko.app.controller.helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.records.Cashbox;
import ua.kapitonenko.app.dao.records.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionWrapperTest {
	
	private SessionWrapper sessionWrapper;
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	
	@Before
	public void setUp() throws Exception {
		when(request.getSession()).thenReturn(session);
		
		sessionWrapper = new SessionWrapper(session);
	}
	
	@Test
	public void setFlash() throws Exception {
		String status = "error";
		String message = "error message";
		
		FlashMessage expected = new FlashMessage(status, message);
		sessionWrapper.setFlash(status, message);
		
		verify(session).setAttribute(Keys.FLASH, expected);
	}
	
	@Test
	public void getFlash() throws Exception {
		String status = "error";
		String message = "error message";
		FlashMessage expected = new FlashMessage(status, message);
		when(session.getAttribute(Keys.FLASH)).thenReturn(expected);
		
		assertThat(sessionWrapper.getFlash(), is(equalTo(expected)));
		verify(session).removeAttribute(Keys.FLASH);
	}
	
	@Test
	public void logout() throws Exception {
		sessionWrapper.logout();
		verify(session).invalidate();
	}
	
	
	@Test
	public void login() throws Exception {
		User user = mock(User.class);
		Cashbox cashbox = mock(Cashbox.class);
		
		sessionWrapper.login(user, cashbox);
		verify(session).setAttribute(Keys.USER, user);
		verify(session).setAttribute(Keys.CASHBOX, cashbox);
	}
	
}