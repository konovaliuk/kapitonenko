package ua.kapitonenko.app.dao;

import org.junit.After;
import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.CashboxDAO;
import ua.kapitonenko.app.dao.tables.CashboxesTable;
import ua.kapitonenko.app.domain.records.Cashbox;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.fixtures.TestConnection;

import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
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
		super.truncateTable();
	}
	
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
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			Cashbox updated = entities.get(0);
			final String FN_NUMBER = "1111111111";
			updated.setFnNumber(FN_NUMBER);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getFnNumber(), is(equalTo(FN_NUMBER)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			
			List<Cashbox> remaining = Collections.singletonList(entities.get(1));
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
			statement.execute("INSERT INTO cashboxes " +
					                  "(id, fn_number, zn_number, make) " +
					                  "VALUES " +
					                  "  (1, 'default', 'default', 'default'), " +
					                  "  (2, '1010101010', 'AT2020202020', 'КРОХА'), " +
					                  "  (3, '0123456789', '12345678910', 'Datecs');"
			);
		} finally {
			TestConnection.getInstance().close(connection);
		}
	}
}

