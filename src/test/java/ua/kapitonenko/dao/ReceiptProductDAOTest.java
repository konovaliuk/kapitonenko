package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import fixtures.TestConnectionPool;
import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.ReceiptProductDAO;
import ua.kapitonenko.dao.tables.ReceiptProductsTable;
import ua.kapitonenko.domain.entities.ReceiptProduct;

import java.math.BigDecimal;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ReceiptProductDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return ReceiptProductsTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
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
		connection = TestConnectionPool.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO `receipt_products` " +
					                  "(id, receipt_id, product_id, quantity) " +
					                  "VALUES " +
					                  "  (1, 1, 1 , 111.111)");
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

