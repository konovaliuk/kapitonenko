package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import fixtures.TestConnectionPool;
import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.dao.interfaces.TaxCategoryDAO;
import ua.kapitonenko.dao.tables.TaxCategoriesTable;
import ua.kapitonenko.domain.entities.TaxCategory;

import java.math.BigDecimal;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TaxCategoryDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return TaxCategoriesTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		TaxCategoryDAO dao = Application.getDAOFactory().getTaxCategoryDAO(connection);
		
		List<TaxCategory> entities = Arrays.asList(
				new TaxCategory(null, "20%", "bundle", "tax.1", new BigDecimal("20.0")),
				new TaxCategory(null, "nontaxable", "settings", "tax.2", new BigDecimal("0.0"))
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			TaxCategory updated = entities.get(0);
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
			statement.execute("INSERT INTO tax_categories " +
					                  "(id, name, bundle_name, bundle_key, rate) VALUES " +
					                  "  (1, '1', 'settings', 'tax.1', 20.0), " +
					                  "  (2, '2', 'settings', 'tax.2', 0);"
			);
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

