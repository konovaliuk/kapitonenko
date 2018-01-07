package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.UnitDAO;
import ua.kapitonenko.app.dao.tables.UnitsTable;
import ua.kapitonenko.app.domain.records.Unit;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnectionPool;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UnitDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return UnitsTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
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
			assertThat(dao.findAll(), is(equalTo(entities)));
			
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
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnectionPool.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO units" +
					                  "(id, name, bundle_name, bundle_key) VALUES" +
					                  "  (1, 'kilogram', 'settings', 'unit.kg')," +
					                  "  (2, 'piece', 'settings', 'pc');"
			);
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

