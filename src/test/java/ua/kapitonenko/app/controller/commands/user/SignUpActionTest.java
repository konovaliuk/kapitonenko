package ua.kapitonenko.app.controller.commands.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.persistence.records.User;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static ua.kapitonenko.app.config.keys.Keys.*;

@RunWith(MockitoJUnitRunner.class)
public class SignUpActionTest extends UserActionTest {
	
	@InjectMocks
	private SignUpAction action = new SignUpAction();
	
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
		
		verify(requestWrapper).setAttribute(LINK, Actions.LOGIN);
		verify(requestWrapper).setAttribute(ROLE_LIST, Application.getServiceFactory().getSettingsService().getRoleList());
		verify(requestWrapper).setAttribute(eq(USER), user.capture());
		assertThat(user.getValue(), is(instanceOf(User.class)));
		verify(requestWrapper).forward(Pages.USER_FORM, Actions.SIGNUP);
	}
	
	@Test
	public void shouldForwardToSignUpPageOnInvalidUsersInput() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getAttribute(USER)).thenReturn(user);
		when(validator.isValid()).thenReturn(false);
		
		action.execute(requestWrapper);
		
		verify(validator).isValid();
		verify(requestWrapper).forward(Pages.USER_FORM, Actions.SIGNUP);
	}
	
	@Test
	public void shouldSaveUserAndRedirectToLogin() throws Exception {
		when(requestWrapper.isPost()).thenReturn(true);
		when(requestWrapper.getAttribute(USER)).thenReturn(user);
		when(validator.isValid()).thenReturn(true);
		when(userService.createAccount(user)).thenReturn(user);
		
		when(user.isActive()).thenReturn(false);
		action.execute(requestWrapper);
		verify(sessionWrapper).setFlash(anyString(), anyString());
		verify(requestWrapper).redirect(Actions.LOGIN);
	}
	
	
}