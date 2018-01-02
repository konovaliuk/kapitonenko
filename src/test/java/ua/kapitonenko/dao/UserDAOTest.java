package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import fixtures.TestConnectionPool;
import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.UserDAO;
import ua.kapitonenko.dao.tables.UsersTable;
import ua.kapitonenko.domain.entities.User;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UserDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return UsersTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		UserDAO dao = Application.getDAOFactory().getUserDAO(connection);
		
		List<User> entities = Arrays.asList(
				new User(USER_ROLE, "admin", "admin"),
				new User(USER_ROLE, "jane", "jane")
		);
		
		try {
			entities.get(0).setActive(true);
			entities.get(0).setActive(true);
			
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			assertThat(dao.findByLoginAndPassword(entities.get(0)), is(equalTo(entities.get(0))));
			assertThat(dao.findByUsername(entities.get(0)), is(equalTo(entities.get(0))));
			
			User updated = dao.findOne(entities.get(0).getId());
			final String USERNAME = "john";
			updated.setUsername(USERNAME);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getUsername(), is(equalTo(USERNAME)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()).getDeletedAt(), is(notNullValue()));
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnectionPool.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO users " +
					                  "(id, user_role_id, username, password_hash, active, created_at) " +
					                  "VALUES (1, 1, 'admin', 'admin', TRUE, NOW())");
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

