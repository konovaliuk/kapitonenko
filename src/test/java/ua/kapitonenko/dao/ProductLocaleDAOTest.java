package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.ProductLocaleDAO;
import ua.kapitonenko.dao.tables.ProductLocaleTable;
import ua.kapitonenko.domain.ProductLocale;

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
	
	@Test
	public void test() throws Exception {
		
		connection.setAutoCommit(false);
		
		ProductLocaleDAO dao = Application.getDAOFactory().getProductLocaleDAO(connection);
		
		List<ProductLocale> entities = Arrays.asList(
				new ProductLocale(1L, 1L, "name", "milk"),
				new ProductLocale(1L, 2L, "name", "молоко")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			ProductLocale updated = entities.get(0);
			String value = "water";
			updated.setPropertyValue(value);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getPropertyValue(), is(equalTo(value)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			Exception exception = null;
			try {
				dao.delete(entities.get(1), USER_ID);
			} catch (UnsupportedOperationException e) {
				exception = e;
			}
			assertThat(exception, is(notNullValue()));
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
}

