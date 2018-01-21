package ua.kapitonenko.app.controller.commands.user;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mock;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.controller.helpers.*;
import ua.kapitonenko.app.dao.records.User;
import ua.kapitonenko.app.fixtures.AnswerWithSelf;
import ua.kapitonenko.app.fixtures.TestServiceFactory;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.UserService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class UserActionTest {
	
	private static ServiceFactory appServiceFactory;
	
	@Mock
	RequestWrapper requestWrapper;
	@Mock
	SessionWrapper sessionWrapper;
	@Mock
	UserService userService;
	@Mock
	protected User user;
	@Mock
	AlertContainer alertContainer;
	@Mock
	private MessageProvider messageProvider;
	
	ValidationBuilder validator;
	
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
		validator = mock(ValidationBuilder.class, new AnswerWithSelf(ValidationBuilder.class));
		when(requestWrapper.getValidator()).thenReturn(validator);
		when(requestWrapper.getSession()).thenReturn(sessionWrapper);
		when(requestWrapper.getAlert()).thenReturn(alertContainer);
		when(requestWrapper.getMessageProvider()).thenReturn(messageProvider);
		
		when(sessionWrapper.userIsGuest()).thenReturn(true);
	}
}
