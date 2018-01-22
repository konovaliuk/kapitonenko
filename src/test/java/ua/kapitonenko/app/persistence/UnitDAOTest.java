package ua.kapitonenko.app.persistence;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.persistence.dao.UnitDAO;
import ua.kapitonenko.app.persistence.records.Unit;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UnitDAOTest extends BaseDAOTest {
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		UnitDAO dao = Application.getDAOFactory().getUnitDAO(connection);
		
		List<Unit> entities = Arrays.asList(
				new Unit(null, "kg", "bundle", "unit.kg"),
				new Unit(null, "pc", "settings", "unit.pc")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<Unit> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
			Unit updated = entities.get(0);
			final String BUNDLE_KEY = "key";
			updated.setBundleKey(BUNDLE_KEY);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getBundleKey(), is(equalTo(BUNDLE_KEY)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(entities.get(1), USER_ID);
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

