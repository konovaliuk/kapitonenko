package ua.kapitonenko.dao;

import fixtures.BaseDAOTest;
import org.junit.Test;
import ua.kapitonenko.Application;
import ua.kapitonenko.dao.interfaces.CompanyDAO;
import ua.kapitonenko.dao.tables.CompaniesTable;
import ua.kapitonenko.domain.entities.Company;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CompanyDAOTest extends BaseDAOTest {
	
	@Override
	protected String getTableName() {
		return CompaniesTable.NAME;
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCRUD() throws Exception {
		
		connection.setAutoCommit(false);
		
		CompanyDAO dao = Application.getDAOFactory().getCompanyDAO(connection);
		
		List<Company> entities = Arrays.asList(
				new Company(null, "123456789011", "settings", "tvshop.name", "tvshop.address"),
				new Company(null, "222222222222", "options", "shop.name", "shop.address")
		);
		
		try {
			assertThat(dao.insert(entities.get(0)), is(true));
			assertThat(entities.get(0).getId(), is(notNullValue()));
			assertThat(dao.findOne(entities.get(0).getId()), is(equalTo(entities.get(0))));
			
			assertThat(dao.insert(entities.get(1)), is(true));
			assertThat(dao.findAll(), is(equalTo(entities)));
			
			Company updated = entities.get(0);
			final String BUNDLE_KEY = "key";
			updated.setBundleName(BUNDLE_KEY);
			assertThat(dao.update(updated), is(true));
			assertThat(dao.findOne(updated.getId()).getBundleName(), is(equalTo(BUNDLE_KEY)));
			assertThat(dao.findOne(updated.getId()), is(equalTo(updated)));
			
			dao.delete(entities.get(1), USER_ID);
			
		} finally {
			connection.rollback();
			connection.close();
		}
	}
	
}

