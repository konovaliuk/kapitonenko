package ua.kapitonenko.app.controller.commands.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.SessionWrapper;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogoutActionTest {
	private LogoutAction action = new LogoutAction();
	
	@Mock
	private RequestWrapper requestWrapper;
	
	@Mock
	private SessionWrapper sessionWrapper;
	
	@Test(expected = MethodNotAllowedException.class)
	public void shouldThrowExceptionIfNotPost() throws Exception {
		when(requestWrapper.isPost()).thenReturn(false);
		
		action.execute(requestWrapper);
	}
	
	@Test()
	public void shouldCallLogoutOnSessionAndRedirectHome() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getSession()).thenReturn(sessionWrapper);
		
		action.execute(requestWrapper);
		
		verify(sessionWrapper).logout();
		verify(requestWrapper).goHome();
	}
	
	
}