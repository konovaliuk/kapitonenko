package ua.kapitonenko.app.persistence;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.persistence.dao.UserDAO;
import ua.kapitonenko.app.persistence.records.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UserDAOTest extends BaseDAOTest {
	
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
			
			List<User> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
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
}

