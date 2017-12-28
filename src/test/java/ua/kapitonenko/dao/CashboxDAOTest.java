package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.CashboxDAO;
import ua.kapitonenko.dao.tables.CashboxesTable;
import ua.kapitonenko.domain.Cashbox;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CashboxDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return CashboxesTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void test() throws Exception {
		
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
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			Cashbox updated = entities.get(0);
			String fnNumber = "1111111111";
			updated.setFnNumber(fnNumber);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getFnNumber(), is(equalTo(fnNumber)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

