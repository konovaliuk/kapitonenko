package ua.kapitonenko.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.interfaces.UserDAO;
import ua.kapitonenko.app.dao.records.User;

import java.sql.Connection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	private UserService userService;
	@Mock
	private User inMemoryUserMock;
	@Mock
	private User persistentUserMock;
	@Mock
	private DAOFactory daoFactoryMock;
	@Mock
	private ConnectionWrapper wrapperMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private UserDAO userDAOMock;
	
	@Before
	public void setUp() throws Exception {
		userService = Application.getServiceFactory().getUserService();
		userService.setDaoFactory(daoFactoryMock);
		when(daoFactoryMock.getConnection()).thenReturn(wrapperMock);
		when(wrapperMock.open()).thenReturn(connectionMock);
		when(daoFactoryMock.getUserDAO(connectionMock)).thenReturn(userDAOMock);
		when(userDAOMock.insert(inMemoryUserMock)).thenReturn(true);
		when(userDAOMock.findOne(anyLong())).thenReturn(persistentUserMock);
	}
	
	@Test
	public void createAccountShouldActivateUserWhenAutoActivationEnabled() throws Exception {
		Application.setAutoActivationMode(true);
		userService.createAccount(inMemoryUserMock);
		verify(inMemoryUserMock).setActive(true);
	}
	
	@Test
	public void createAccountShouldNotActivateUserWhenAutoActivationDisabled() throws Exception {
		Application.setAutoActivationMode(false);
		userService.createAccount(inMemoryUserMock);
		verify(inMemoryUserMock, never()).setActive(true);
	}
	
	@Test
	public void createAccountShouldInsertUserAndReturnInsertedUser() throws Exception {
		User actual = userService.createAccount(inMemoryUserMock);
		verify(userDAOMock).insert(inMemoryUserMock);
		assertThat(actual, is(equalTo(persistentUserMock)));
	}
	
	@Test
	public void createAccountShouldReturnNullWhenInsertionFailed() throws Exception {
		when(userDAOMock.insert(inMemoryUserMock)).thenReturn(false);
		User actual = userService.createAccount(inMemoryUserMock);
		assertThat(actual, is(nullValue()));
	}
	
	
	@Test
	public void findByLoginAndPasswordShouldReturnExistingUser() throws Exception {
		when(userDAOMock.findByLoginAndPassword(inMemoryUserMock)).thenReturn(persistentUserMock);
		User actual = userService.findByLoginAndPassword(inMemoryUserMock);
		assertThat(actual, is(equalTo(persistentUserMock)));
	}
	
	@Test
	public void findByLoginAndPasswordShouldReturnNullIfNoUserFound() throws Exception {
		when(userDAOMock.findByLoginAndPassword(inMemoryUserMock)).thenReturn(null);
		User actual = userService.findByLoginAndPassword(inMemoryUserMock);
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void findByUsernameShouldReturnExistingUser() throws Exception {
		when(userDAOMock.findByUsername(inMemoryUserMock)).thenReturn(persistentUserMock);
		User actual = userService.findByUsername(inMemoryUserMock);
		assertThat(actual, is(equalTo(persistentUserMock)));
	}
	
	@Test
	public void findByUsernameShouldReturnNullIfNoUserFound() throws Exception {
		when(userDAOMock.findByUsername(inMemoryUserMock)).thenReturn(null);
		User actual = userService.findByUsername(inMemoryUserMock);
		assertThat(actual, is(nullValue()));
	}
	
}