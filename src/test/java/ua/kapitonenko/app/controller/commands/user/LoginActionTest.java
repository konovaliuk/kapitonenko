package ua.kapitonenko.app.controller.commands.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.dao.records.User;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static ua.kapitonenko.app.config.keys.Keys.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActionTest extends UserActionTest {
	
	@InjectMocks
	private LoginAction action = new LoginAction();
	
	@Test
	public void shouldRedirectHomeIfUserLoggedIn() throws Exception {
		when(sessionWrapper.userIsGuest()).thenReturn(false);
		
		action.execute(requestWrapper);
		
		verify(requestWrapper).goHome();
	}
	
	@Test
	public void shouldInitRequestAttrAndForwardToLoginPage() throws Exception {
		when(requestWrapper.getAttribute(USER)).thenReturn(null);
		ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
		
		action.execute(requestWrapper);
		
		verify(requestWrapper).setAttribute(LINK, Actions.SIGNUP);
		verify(requestWrapper).setAttribute(eq(CASHBOX), any());
		verify(requestWrapper).setAttribute(eq(USER), user.capture());
		assertThat(user.getValue(), is(instanceOf(User.class)));
		verify(requestWrapper).forward(Pages.USER_FORM, Actions.LOGIN);
	}
	
	@Test
	public void shouldForwardToLoginPageOnInvalidUsersInput() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getAttribute(USER)).thenReturn(user);
		when(validator.isValid()).thenReturn(false);
		
		action.execute(requestWrapper);
		
		verify(validator).isValid();
		verify(requestWrapper).forward(Pages.USER_FORM, Actions.LOGIN);
	}
	
	@Test
	public void shouldInitAlertErrorAndForwardToLoginPageIfUserNotFound() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getAttribute(USER)).thenReturn(user);
		when(validator.isValid()).thenReturn(true);
		when(userService.findByLoginAndPassword(any(User.class))).thenReturn(null);
		
		action.execute(requestWrapper);
		
		verify(alertContainer).addMessage(anyString());
		verify(alertContainer).setMessageType(Keys.ALERT_CLASS_DANGER);
		verify(requestWrapper).forward(Pages.USER_FORM, Actions.LOGIN);
	}
	
	@Test
	public void shouldLoginExistingUserAndRedirectToHome() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getAttribute(USER)).thenReturn(user);
		when(validator.isValid()).thenReturn(true);
		when(userService.findByLoginAndPassword(any(User.class))).thenReturn(user);
		
		action.execute(requestWrapper);
		verify(sessionWrapper).login(any(), any());
		verify(requestWrapper).goHome();
	}
	
	
}