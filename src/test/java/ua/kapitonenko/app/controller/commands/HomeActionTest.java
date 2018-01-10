package ua.kapitonenko.app.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.SessionWrapper;
import ua.kapitonenko.app.domain.records.User;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HomeActionTest {
	private HomeAction homeAction = new HomeAction();
	
	@Mock
	private RequestWrapper requestWrapper;
	
	@Mock
	private SessionWrapper sessionWrapper;
	
	@Mock
	private User user;
	
	@Before
	public void setUp() throws Exception {
		when(requestWrapper.getSession()).thenReturn(sessionWrapper);
		when(sessionWrapper.userIsGuest()).thenReturn(false);
		when(sessionWrapper.getUser()).thenReturn(user);
	}
	
	@Test
	public void shouldRedirectToLoginWhenUserIsGuest() throws Exception {
		when(sessionWrapper.userIsGuest()).thenReturn(true);
		
		homeAction.execute(requestWrapper);
		
		verify(requestWrapper).redirect(Actions.LOGIN);
	}
	
	@Test
	public void shouldRedirectToProductsWhenUserIsMerchandiser() throws Exception {
		when(user.getUserRoleId()).thenReturn(Application.getId(Application.ROLE_MERCHANDISER));
		
		homeAction.execute(requestWrapper);
		
		verify(requestWrapper).redirect(Actions.PRODUCTS);
	}
	
	@Test
	public void shouldRedirectToReportsWhenUserIsSenior() throws Exception {
		when(user.getUserRoleId()).thenReturn(Application.getId(Application.ROLE_SENIOR));
		
		homeAction.execute(requestWrapper);
		
		verify(requestWrapper).redirect(Actions.REPORTS);
	}
	
	@Test
	public void shouldRedirectToReceiptsWhenUserIsCashier() throws Exception {
		when(user.getUserRoleId()).thenReturn(Application.getId(Application.ROLE_CASHIER));
		
		homeAction.execute(requestWrapper);
		
		verify(requestWrapper).redirect(Actions.RECEIPTS);
	}
	
}