package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import org.junit.Test;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.dao.interfaces.ZReportDAO;
import ua.kapitonenko.dao.tables.ZReportsTable;
import ua.kapitonenko.domain.entities.ZReport;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ZReportDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return ZReportsTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		super.truncateTable();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ZReportDAO dao = Application.getDAOFactory().getZReportDAO(connection);
		
		List<ZReport> entities = Arrays.asList(
				new ZReport(null, CASHBOX, RECEIPT_ID, BigDecimal.valueOf(0.99), USER_ID),
				new ZReport(null, CASHBOX, RECEIPT_ID, BigDecimal.valueOf(1000.01), USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			ZReport updated = entities.get(0);
			final BigDecimal CASH = new BigDecimal("0.00");
			updated.setCashBalance(CASH);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getCashBalance(), is(CASH));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(entities.get(1), USER_ID);
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

