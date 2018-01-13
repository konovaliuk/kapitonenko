package ua.kapitonenko.app.dao;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.TaxCategoryDAO;
import ua.kapitonenko.app.domain.records.TaxCategory;
import ua.kapitonenko.app.fixtures.BaseDAOTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TaxCategoryDAOTest extends BaseDAOTest {
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		TaxCategoryDAO dao = Application.getDAOFactory().getTaxCategoryDAO(connection);
		
		List<TaxCategory> entities = Arrays.asList(
				new TaxCategory(null, "20%", "bundle", "tax.1", new BigDecimal("20.0")),
				new TaxCategory(null, "nontaxable", "settings", "tax.2", new BigDecimal("0.0"))
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<TaxCategory> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
			TaxCategory updated = entities.get(0);
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

