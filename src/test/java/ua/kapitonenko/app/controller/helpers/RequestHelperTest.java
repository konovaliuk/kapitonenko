package ua.kapitonenko.app.controller.helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.commands.HomeAction;
import ua.kapitonenko.app.controller.commands.report.ReportListAction;
import ua.kapitonenko.app.exceptions.ForbiddenException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.persistence.records.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestHelperTest {
	private static final String URI = "login.jsp";
	private static final String ACTION = "/login";
	
	private RequestHelper requestHelper;
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private RequestWrapper requestWrapper;
	@Mock
	private SessionWrapper sessionWrapper;
	@Mock
	private HttpSession session;
	@Mock
	private User user;
	
	
	@Before
	public void setUp() throws Exception {
		requestHelper = RequestHelper.getInstance();
		
		when(request.getSession()).thenReturn(session);
		when(requestWrapper.getSession()).thenReturn(sessionWrapper);
		when(sessionWrapper.getUser()).thenReturn(user);
	}
	
	@Test(expected = NotFoundException.class)
	public void getCommandShouldThrowExceptionOnNotExistingUri() throws Exception {
		when(requestWrapper.getUri()).thenReturn("");
		requestHelper.getCommand(requestWrapper);
	}
	
	@Test(expected = ForbiddenException.class)
	public void getCommandShouldThrowExceptionOnUnauthorizedUser() throws Exception {
		when(user.getUserRoleId()).thenReturn(Application.Ids.ROLE_MERCHANDISER.getValue());
		when(requestWrapper.getUri()).thenReturn(Actions.REPORTS);
		requestHelper.getCommand(requestWrapper);
	}
	
	@Test
	public void getCommandShouldReturnHomeForGuests() throws Exception {
		when(requestWrapper.getUri()).thenReturn(Actions.REPORTS);
		when(sessionWrapper.getUser()).thenReturn(null);
		
		ActionCommand command = requestHelper.getCommand(requestWrapper);
		assertThat(command, is(instanceOf(HomeAction.class)));
	}
	
	@Test
	public void getCommandShouldReturnActionCommand() throws Exception {
		when(requestWrapper.getUri()).thenReturn(Actions.REPORTS);
		when(user.getUserRoleId()).thenReturn(Application.Ids.ROLE_SENIOR.getValue());
		
		ActionCommand command = requestHelper.getCommand(requestWrapper);
		assertThat(command, is(instanceOf(ReportListAction.class)));
	}
	
}