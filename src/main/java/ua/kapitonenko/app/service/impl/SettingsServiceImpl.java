package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.domain.records.*;
import ua.kapitonenko.app.service.SettingsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.kapitonenko.app.config.keys.Keys.*;

public class SettingsServiceImpl implements SettingsService {
	private static final Logger LOGGER = Logger.getLogger(SettingsService.class);
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	private HashMap<String, Object> cache = new HashMap<>();
	
	@Override
	public void clearCache() {
		cache = new HashMap<>();
	}
	
	@Override
	public List<UserRole> getRoleList() {
		if (!cache.containsKey(ROLE_LIST)) {
			
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				UserRoleDAO roleDAO = daoFactory.getUserRoleDAO(connection.open());
				List<UserRole> list = roleDAO.findAll();
				cache.put(ROLE_LIST, list);
			}
		}
		return (List<UserRole>) cache.get(ROLE_LIST);
	}
	
	
	@Override
	public List<TaxCategory> getTaxCatList() {
		if (!cache.containsKey(TAX_CAT_LIST)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				TaxCategoryDAO taxCategoryDAO = daoFactory.getTaxCategoryDAO(connection.open());
				List<TaxCategory> list = taxCategoryDAO.findAll();
				cache.put(TAX_CAT_LIST, list);
			}
		}
		return (List<TaxCategory>) cache.get(TAX_CAT_LIST);
	}
	
	@Override
	public List<Unit> getUnitList() {
		if (!cache.containsKey(UNIT_LIST)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				UnitDAO unitDAO = daoFactory.getUnitDAO(connection.open());
				List<Unit> list = unitDAO.findAll();
				cache.put(UNIT_LIST, list);
			}
		}
		return (List<Unit>) cache.get(UNIT_LIST);
	}
	
	@Override
	public Cashbox findCashbox(Long id) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			CashboxDAO cashboxDAO = daoFactory.getCashboxDao(connection.open());
			return cashboxDAO.findOne(id);
		}
	}
	
	
	@Override
	public List<LocaleRecord> getLocaleList() {
		if (!cache.containsKey(LANGUAGES)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				LocaleDAO localeDAO = daoFactory.getLocaleDAO(connection.open());
				List<LocaleRecord> list = localeDAO.findAll();
				
				cache.put(LANGUAGES, list);
			}
		}
		return (List<LocaleRecord>) cache.get(LANGUAGES);
	}
	
	@Override
	public List<String> getSupportedLanguages() {
		return getSupportedLocales().keySet().stream().sorted().collect(Collectors.toList());
	}
	
	@Override
	public Map<String, LocaleRecord> getSupportedLocales() {
		return getLocaleList().stream()
				       .collect(Collectors.toMap(LocaleRecord::getLanguage, Function.identity()));
	}
	
	@Override
	public Company findCompany(Long id) {
		if (!cache.containsKey(COMPANY)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				CompanyDAO companyDAO = daoFactory.getCompanyDAO(connection.open());
				cache.put(COMPANY, companyDAO.findOne(id));
			}
		}
		return (Company) cache.get(COMPANY);
	}
	
	@Override
	public List<PaymentType> getPaymentTypes() {
		if (!cache.containsKey(PAYMENT_TYPES)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				PaymentTypeDAO paymentTypeDAO = daoFactory.getPaymentTypeDAO(connection.open());
				
				List<PaymentType> list = paymentTypeDAO.findAll();
				cache.put(PAYMENT_TYPES, list);
			}
		}
		return (List<PaymentType>) cache.get(PAYMENT_TYPES);
	}
	
	@Override
	public List<Cashbox> getCashboxList() {
		if (!cache.containsKey(CASHBOX_LIST)) {
			try (ConnectionWrapper connection = daoFactory.getConnection()) {
				CashboxDAO cashboxDAO = daoFactory.getCashboxDao(connection.open());
				
				List<Cashbox> list = cashboxDAO.findAll();
				cache.put(CASHBOX_LIST, list);
			}
		}
		return (List<Cashbox>) cache.get(CASHBOX_LIST);
	}
}
