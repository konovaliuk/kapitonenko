package ua.kapitonenko.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.persistence.connection.ConnectionWrapper;
import ua.kapitonenko.app.persistence.dao.*;
import ua.kapitonenko.app.persistence.records.*;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsServiceTest {
	private SettingsService settingsService;
	
	@Mock
	private DAOFactory daoFactory;
	@Mock
	private ConnectionWrapper wrapper;
	@Mock
	private Connection connection;
	@Mock
	private PaymentTypeDAO paymentTypeDAO;
	@Mock
	private UserRoleDAO userRoleDAO;
	@Mock
	private TaxCategoryDAO taxCategoryDAO;
	@Mock
	private UnitDAO unitDAO;
	@Mock
	private CashboxDAO cashboxDAO;
	@Mock
	private LocaleDAO localeDAO;
	@Mock
	private CompanyDAO companyDAO;
	
	@Before
	public void setUp() throws Exception {
		settingsService = Application.getServiceFactory().getSettingsService();
		settingsService.setDaoFactory(daoFactory);
		when(daoFactory.getConnection()).thenReturn(wrapper);
		when(wrapper.open()).thenReturn(connection);
		
		when(daoFactory.getCashboxDao(connection)).thenReturn(cashboxDAO);
		when(daoFactory.getPaymentTypeDAO(connection)).thenReturn(paymentTypeDAO);
		when(daoFactory.getLocaleDAO(connection)).thenReturn(localeDAO);
		when(daoFactory.getUnitDAO(connection)).thenReturn(unitDAO);
		when(daoFactory.getCompanyDAO(connection)).thenReturn(companyDAO);
		when(daoFactory.getTaxCategoryDAO(connection)).thenReturn(taxCategoryDAO);
		when(daoFactory.getUserRoleDAO(connection)).thenReturn(userRoleDAO);
		
		settingsService.clearCache();
	}
	
	@Test
	public void clearCache() throws Exception {
	}
	
	@Test
	public void getRoleList() throws Exception {
		int listSize = 9;
		when(userRoleDAO.findAll()).thenReturn(generateList(UserRole.class, listSize));
		
		assertThat(settingsService.getRoleList().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void getTaxCatList() throws Exception {
		int listSize = 6;
		when(taxCategoryDAO.findAll()).thenReturn(generateList(TaxCategory.class, listSize));
		
		assertThat(settingsService.getTaxCatList().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void getUnitList() throws Exception {
		int listSize = 1;
		when(unitDAO.findAll()).thenReturn(generateList(Unit.class, listSize));
		
		assertThat(settingsService.getUnitList().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void findCashbox() throws Exception {
		when(cashboxDAO.findOne(anyLong())).thenReturn(mock(Cashbox.class));
		assertThat(settingsService.findCashbox(ModelUtils.anyLong()), is(notNullValue()));
	}
	
	@Test
	public void getLocaleList() throws Exception {
		int listSize = 9;
		when(localeDAO.findAll()).thenReturn(generateList(LocaleRecord.class, listSize));
		
		assertThat(settingsService.getLocaleList().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void getSupportedLocalesAndLanguages() throws Exception {
		int listSize = 9;
		List<LocaleRecord> list = Stream.generate(() -> {
			LocaleRecord record = mock(LocaleRecord.class);
			when(record.getLanguage()).thenReturn(ModelUtils.anyLong().toString());
			return record;
		}).limit(listSize).collect(Collectors.toList());
		
		when(localeDAO.findAll()).thenReturn(list);
		
		assertThat(settingsService.getSupportedLocales().size(), is(equalTo(listSize)));
		assertThat(settingsService.getSupportedLanguages().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void findCompany() throws Exception {
		when(companyDAO.findOne(anyLong())).thenReturn(mock(Company.class));
		assertThat(settingsService.findCompany(ModelUtils.anyLong()), is(notNullValue()));
	}
	
	@Test
	public void getPaymentTypes() throws Exception {
		int listSize = 2;
		when(paymentTypeDAO.findAll()).thenReturn(generateList(PaymentType.class, listSize));
		
		assertThat(settingsService.getPaymentTypes().size(), is(equalTo(listSize)));
	}
	
	@Test
	public void getCashboxList() throws Exception {
		int listSize = 5;
		when(cashboxDAO.findAll()).thenReturn(generateList(Cashbox.class, listSize));
		
		assertThat(settingsService.getCashboxList().size(), is(equalTo(listSize)));
	}
	
	private <E> List<E> generateList(Class<E> recordClass, int size) {
		return Stream.generate(() -> mock(recordClass))
				       .limit(size)
				       .collect(Collectors.toList());
	}
}