package ua.kapitonenko.app.controller.commands;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.SessionWrapper;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.fixtures.TestServiceFactory;
import ua.kapitonenko.app.service.ServiceFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageActionTest {
	private LanguageAction action = new LanguageAction();
	
	@Mock
	private RequestWrapper requestWrapper;
	
	@Mock
	private SessionWrapper sessionWrapper;
	
	private static ServiceFactory appServiceFactory;
	
	@BeforeClass
	public static void configApp() {
		appServiceFactory = Application.getServiceFactory();
		Application.setServiceFactory(TestServiceFactory.getInstance());
	}
	
	@AfterClass
	public static void resetConfig() {
		Application.setServiceFactory(appServiceFactory);
	}
	
	@Before
	public void setUp() throws Exception {
		when(requestWrapper.getSession()).thenReturn(sessionWrapper);
		when(requestWrapper.isPost()).thenReturn(true);
	}
	
	@Test(expected = MethodNotAllowedException.class)
	public void shouldThrowExceptionIfNotPost() throws Exception {
		when(requestWrapper.isPost()).thenReturn(false);
		
		action.execute(requestWrapper);
	}
	
	@Test(expected = NotFoundException.class)
	public void shouldThrowExceptionIfLanguageNotSupported() throws Exception {
		when(requestWrapper.getParameter(Keys.LANG)).thenReturn("fr");
		
		action.execute(requestWrapper);
	}
	
	@Test
	public void shouldChangeSessionLocaleAndRedirectBack() throws Exception {
		when(requestWrapper.getParameter(Keys.LANG)).thenReturn("uk");
		
		action.execute(requestWrapper);
		
		verify(sessionWrapper).set(eq(Keys.LOCALE), any());
		verify(sessionWrapper).set(eq(Keys.LOCALE_ID), any());
		verify(requestWrapper).goBack();
	}
	
	
}