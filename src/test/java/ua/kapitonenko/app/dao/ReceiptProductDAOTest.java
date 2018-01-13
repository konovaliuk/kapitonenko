package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ReceiptProductDAO;
import ua.kapitonenko.app.domain.records.ReceiptProduct;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.math.BigDecimal;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ReceiptProductDAOTest extends BaseDAOTest {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (1, 1, 1, 1, 0, NOW(), 1)");
			statement.execute("INSERT INTO products\n" +
					                  "(id, unit_id, price, tax_category_id, quantity, created_at, created_by, deleted_at, deleted_by)\n" +
					                  "VALUES (1, 1, 9.99, 1, 999.999, NOW(), 1, NULL, NULL)"
			);
		}
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ReceiptProductDAO dao = Application.getDAOFactory().getReceiptProductDAO(connection);
		
		List<ReceiptProduct> entities = Arrays.asList(
				new ReceiptProduct(null, RECEIPT_ID, PRODUCT_ID, new BigDecimal("500.999")),
				new ReceiptProduct(null, RECEIPT_ID, PRODUCT_ID, new BigDecimal("999.500"))
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			ReceiptProduct updated = entities.get(0);
			final BigDecimal QUANTITY = new BigDecimal("0.000");
			updated.setQuantity(QUANTITY);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getQuantity(), is(equalTo(QUANTITY)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(updated, USER_ID);
			
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnection.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("DELETE FROM receipts");
			statement.execute("DELETE FROM products");
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

