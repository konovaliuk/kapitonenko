package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ReceiptDAO;
import ua.kapitonenko.app.dao.records.ReceiptRecord;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ReceiptDAOTest extends BaseDAOTest {
	
	ReceiptDAO dao;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		try (Statement statement = connection.createStatement()) {
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (1, 1, 1, 1, 0, NOW(), 1)");
			
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (2, 2, 1, 1, 0, '2017-01-19 03:00:00', 1)");
			
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (3, 2, 1, 1, 1, '2017-01-20 03:00:00', 1)");
			
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (4, 1, 1, 1, 0, NOW(), 1)");
			
			statement.execute("INSERT INTO receipts\n" +
					                  "(id, cashbox_id, payment_type_id, receipt_type_id, cancelled, created_at, created_by)\n" +
					                  "VALUES (5, 2, 1, 1, 1, NOW(), 1)");
			
			statement.execute("INSERT INTO z_reports\n" +
					                  "(id, cashbox_id, cash_balance, created_at, created_by)\n" +
					                  "VALUES (1, 2, 500.76, '2017-01-19 03:14:07', 1)");
			
			statement.execute("INSERT INTO z_reports\n" +
					                  "(id, cashbox_id, cash_balance, created_at, created_by)\n" +
					                  "VALUES (2, 2, 633.76, '2017-01-20 03:14:07', 1)");
			
		}
		
		dao = Application.getDAOFactory().getReceiptDAO(connection);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		List<ReceiptRecord> entities = Arrays.asList(
				new ReceiptRecord(null, CASHBOX_2, PAYMENT, RECEIPT_TYPE, false, USER_ID),
				new ReceiptRecord(null, CASHBOX_2, PAYMENT, RECEIPT_TYPE, true, USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<ReceiptRecord> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
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
	
	@Test
	public void findAllOffsetLimitShouldReturnListInDescOrder() throws Exception {
		final int OFFSET = 1;
		final int LIMIT = 2;
		
		List<ReceiptRecord> list = dao.findAll(OFFSET, LIMIT);
		assertThat(list.size(), is(equalTo(LIMIT)));
		assertThat(list.get(0).getId(), is(equalTo(4L)));
		assertThat(list.get(1).getId(), is(equalTo(3L)));
		
	}
	
	@Test
	public void findAllByCashboxIdShouldReturnNotReportedReceipts() throws Exception {
		final Long CASHBOX_ID = 2L;
		
		List<ReceiptRecord> list = dao.findAllByCashboxId(CASHBOX_ID);
		assertThat(list.size(), is(equalTo(1)));
		assertThat(list.get(0).getCashboxId(), is(equalTo(CASHBOX_ID)));
		assertThat(list.get(0).getId(), is(equalTo(5L)));
	}
	
	@Test
	public void findAllByZReportIdShouldReturnReportedReceipts() throws Exception {
		final Long Z_REPORT_ID = 2L;
		final Long CASHBOX_ID = 2L;
		
		List<ReceiptRecord> list = dao.findAllByZReportId(Z_REPORT_ID, CASHBOX_ID);
		assertThat(list.size(), is(equalTo(1)));
		assertThat(list.get(0).getCashboxId(), is(equalTo(CASHBOX_ID)));
		assertThat(list.get(0).getId(), is(equalTo(3L)));
	}
	
	@After
	public void tearDown() throws Exception {
		connection = TestConnection.getInstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			statement.execute("DELETE FROM receipts");
			statement.execute("DELETE FROM z_reports");
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

