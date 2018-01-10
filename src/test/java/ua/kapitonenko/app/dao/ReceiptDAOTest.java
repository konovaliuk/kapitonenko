package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ReceiptDAO;
import ua.kapitonenko.app.dao.tables.ReceiptsTable;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ReceiptDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return ReceiptsTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ReceiptDAO dao = Application.getDAOFactory().getReceiptDAO(connection);
		
		List<ReceiptRecord> entities = Arrays.asList(
				new ReceiptRecord(null, CASHBOX, PAYMENT, RECEIPT_TYPE, false, USER_ID),
				new ReceiptRecord(null, CASHBOX, PAYMENT, RECEIPT_TYPE, true, USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			ReceiptRecord updated = entities.get(0);
			final boolean CANCELLED = true;
			updated.setCancelled(CANCELLED);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).isCancelled(), is(CANCELLED));
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
			statement.execute("INSERT INTO `receipts` " +
					                  "(`id`, `cashbox_id`, `payment_type_id`, `receipt_type_id`, `cancelled`, `created_at`, `created_by`) " +
					                  "VALUES " +
					                  "  (1, 1, 1, 1, FALSE , NOW(), 1), " +
					                  "  (2, 2, 1, 1, TRUE , NOW(), 1);"
			);
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

