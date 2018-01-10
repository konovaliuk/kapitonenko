package ua.kapitonenko.app.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.SessionWrapper;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageActionTest {
	private LanguageAction action = new LanguageAction();
	
	@Mock
	private RequestWrapper requestWrapper;
	
	@Mock
	private SessionWrapper sessionWrapper;
	
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
}