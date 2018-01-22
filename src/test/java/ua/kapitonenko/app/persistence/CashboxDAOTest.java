package ua.kapitonenko.app.persistence;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.persistence.dao.CashboxDAO;
import ua.kapitonenko.app.persistence.records.Cashbox;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CashboxDAOTest extends BaseDAOTest {
	
	
	@Test
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		CashboxDAO dao = Application.getDAOFactory().getCashboxDao(connection);
		
		List<Cashbox> entities = Arrays.asList(
				new Cashbox("0123456789", "012345678910", "Datecs"),
				new Cashbox("0000000000", "555555555555", "КРОХА")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<Cashbox> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
			Cashbox updated = entities.get(0);
			final String FN_NUMBER = "1111111111";
			updated.setFnNumber(FN_NUMBER);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getFnNumber(), is(equalTo(FN_NUMBER)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			assertThat(dao.findAll().contains(entities.get(0)), is(false));
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

