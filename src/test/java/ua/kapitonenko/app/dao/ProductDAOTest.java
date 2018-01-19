package ua.kapitonenko.app.dao;

import org.junit.Test;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.ProductDAO;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.fixtures.BaseDAOTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductDAOTest extends BaseDAOTest {
	
	@Test
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ProductDAO dao = Application.getDAOFactory().getProductDAO(connection);
		
		List<ProductRecord> entities = Arrays.asList(
				new ProductRecord(UNIT, new BigDecimal("9.99"), TAX_1, new BigDecimal("999.999"), USER_ID),
				new ProductRecord(UNIT, new BigDecimal("33.33"), TAX_2, new BigDecimal("1000.000"), USER_ID)
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			assertThat(dao.insert(entities.get(1)), is(true));
			
			List<ProductRecord> list = dao.findAll();
			assertThat(list.size(), is(greaterThanOrEqualTo(entities.size())));
			assertThat(list, hasItems(entities.get(0), entities.get(1)));
			
			ProductRecord updated = entities.get(0);
			final BigDecimal PRICE = new BigDecimal("0.00");
			updated.setPrice(PRICE);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getPrice(), is(equalTo(PRICE)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			
			assertThat(dao.delete(updated, USER_ID), is(true));
			assertThat(dao.findOne(updated.getId()), is(nullValue()));
			assertThat(dao.findAll().contains(entities.get(0)), is(false));
			
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

