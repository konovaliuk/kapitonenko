package ua.kapitonenko.app.persistence;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.BaseDAOTest;
import ua.kapitonenko.app.persistence.dao.PaymentTypeDAO;
import ua.kapitonenko.app.persistence.records.PaymentType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PaymentTypeDAOTest extends BaseDAOTest {
	
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
			
			List<PaymentType> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
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
}

