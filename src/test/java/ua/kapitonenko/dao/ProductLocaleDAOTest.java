package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import org.junit.Test;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.dao.interfaces.ProductLocaleDAO;
import ua.kapitonenko.dao.tables.ProductLocaleTable;
import ua.kapitonenko.domain.entities.ProductLocale;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ProductLocaleDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return ProductLocaleTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		ProductLocaleDAO dao = Application.getDAOFactory().getProductLocaleDAO(connection);
		
		List<ProductLocale> entities = Arrays.asList(
				new ProductLocale(PRODUCT_ID, 1L, "name", "milk"),
				new ProductLocale(PRODUCT_ID, 2L, "name", "молоко")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			ProductLocale updated = entities.get(0);
			final String VALUE = "water";
			updated.setPropertyValue(VALUE);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getPropertyValue(), is(equalTo(VALUE)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(entities.get(1), USER_ID);
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

