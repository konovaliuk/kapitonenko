package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import fixtures.TestConnectionPool;
import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.PaymentTypeDAO;
import ua.kapitonenko.dao.tables.PaymentTypesTable;
import ua.kapitonenko.domain.entities.PaymentType;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PaymentTypeDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return PaymentTypesTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		PaymentTypeDAO dao = Application.getDAOFactory().getPaymentTypeDAO(connection);
		
		List<PaymentType> entities = Arrays.asList(
				new PaymentType(null, "cash", "settings", "type.cash"),
				new PaymentType(null, "card", "messages", "type.card")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			PaymentType updated = entities.get(0);
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
			statement.execute("INSERT INTO `payment_types` " +
					                  "(id, name, bundle_name, bundle_key) " +
					                  "VALUES " +
					                  "  (1, 'cash', 'settings', 'payment.type.cash');"
			);
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}

