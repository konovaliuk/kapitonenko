package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ProductDAO;
import ua.kapitonenko.app.dao.tables.ProductsTable;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.math.BigDecimal;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return ProductsTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ProductDAO dao = Application.getDAOFactory().getProductDAO(connection);
		
		List<Product> entities = Arrays.asList(
				new Product(UNIT_1, new BigDecimal("9.99"), TAX_1, new BigDecimal("999.999"), USER_ID),
				new Product(UNIT_1, new BigDecimal("33.33"), TAX_2, new BigDecimal("1000.000"), USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			Product updated = entities.get(0);
			final BigDecimal PRICE = new BigDecimal("0.00");
			updated.setPrice(PRICE);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getPrice(), is(equalTo(PRICE)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			
			List<Product> remaining = Collections.singletonList(entities.get(1));
			assertThat(dao.findAll(), is(equalTo(remaining)));
			
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnection.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO products " +
					                  "(id, unit_id, price, tax_category_id, quantity, created_at, created_by, deleted_at, deleted_by) " +
					                  "VALUES (1, 1, 9.99, 1, 999.999, NOW(), 1, NULL, NULL)");
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

