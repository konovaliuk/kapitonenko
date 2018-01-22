package ua.kapitonenko.app.persistence;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.persistence.dao.ZReportDAO;
import ua.kapitonenko.app.persistence.records.ZReport;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ZReportDAOTest extends BaseDAOTest {
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ZReportDAO dao = Application.getDAOFactory().getZReportDAO(connection);
		
		List<ZReport> entities = Arrays.asList(
				new ZReport(null, CASHBOX_2, BigDecimal.valueOf(0.99), USER_ID),
				new ZReport(null, CASHBOX_2, BigDecimal.valueOf(1000.01), USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<ZReport> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
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

