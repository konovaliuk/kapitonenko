package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import fixtures.TestConnectionPool;
import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.UserRoleDAO;
import ua.kapitonenko.dao.tables.UserRoleTable;
import ua.kapitonenko.domain.UserRole;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UserRoleDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return UserRoleTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test
	public void test() throws Exception {
		
		connection.setAutoCommit(false);
		
		UserRoleDAO dao = Application.getDAOFactory().getUserRoleDAO(connection);
		
		List<UserRole> entities = Arrays.asList(
				new UserRole(null, "admin", "messages", "role.admin"),
				new UserRole(null, "merchandiser", "options", "role.merchandiser")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			UserRole updated = entities.get(0);
			String key = "keys";
			updated.setBundleKey(key);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getBundleKey(), is(equalTo(key)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			Exception exception = null;
			try {
				dao.delete(entities.get(1), USER_ID);
			} catch (UnsupportedOperationException e) {
				exception = e;
			}
			assertThat(exception, is(notNullValue()));
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnectionPool.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO user_roles " +
					                  "(id, name, bundle_name, bundle_key) " +
					                  "VALUES (1, 'admin', 'messages', 'role.admin');"
			);
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

