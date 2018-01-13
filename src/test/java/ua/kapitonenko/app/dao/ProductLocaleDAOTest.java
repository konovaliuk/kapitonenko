package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ProductLocaleDAO;
import ua.kapitonenko.app.domain.records.ProductLocale;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductLocaleDAOTest extends BaseDAOTest {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO products\n" +
					                  "(id, unit_id, price, tax_category_id, quantity, created_at, created_by, deleted_at, deleted_by)\n" +
					                  "VALUES (1, 1, 9.99, 1, 999.999, NOW(), 1, NULL, NULL)"
			);
		}
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ProductLocaleDAO dao = Application.getDAOFactory().getProductLocaleDAO(connection);
		
		List<ProductLocale> entities = Arrays.asList(
				new ProductLocale(PRODUCT_ID, 1L, "name", "milk"),
				new ProductLocale(PRODUCT_ID, 2L, "name", "молоко")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<ProductLocale> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
			ProductLocale updated = entities.get(0);
			final String VALUE = "water";
			updated.setPropertyValue(VALUE);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getPropertyValue(), is(equalTo(VALUE)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(entities.get(1), USER_ID);
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnection.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("DELETE FROM products");
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

