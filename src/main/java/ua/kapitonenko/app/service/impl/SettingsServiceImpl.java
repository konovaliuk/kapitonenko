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

public class SettingsServiceImpl implements SettingsService {
	private static final Logger LOGGER = Logger.getLogger(SettingsService.class);
	
	// TODO add to cache
	private HashMap<String, Object> cache = new HashMap<>();
	
	@Override
	public List<UserRole> getRoleList() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UserRoleDAO roleDAO = Application.getDAOFactory().getUserRoleDAO(connection.open());
			List<UserRole> list = roleDAO.findAll();
			return list;
		}
	}
	
	@Override
	public Map<Long, String> getTaxMap() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			TaxCategoryDAO taxCategoryDAO = Application.getDAOFactory().getTaxCategoryDAO(connection.open());
			List<TaxCategory> list = taxCategoryDAO.findAll();
			LOGGER.debug(list);
			return list.stream().collect(
					Collectors.toMap(BaseEntity::getId, BaseLocalizedEntity::getBundleKey));
		}
	}
	
	@Override
	public List<TaxCategory> getTaxCatList() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			TaxCategoryDAO taxCategoryDAO = Application.getDAOFactory().getTaxCategoryDAO(connection.open());
			List<TaxCategory> list = taxCategoryDAO.findAll();
			return list;
		}
	}
	
	@Override
	public Map<Long, String> getUnitMap() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UnitDAO unitDAO = Application.getDAOFactory().getUnitDAO(connection.open());
			List<Unit> list = unitDAO.findAll();
			
			return list.stream().collect(
					Collectors.toMap(BaseEntity::getId, BaseLocalizedEntity::getBundleKey));
		}
	}
	
	@Override
	public List<Unit> getUnitList() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UnitDAO unitDAO = Application.getDAOFactory().getUnitDAO(connection.open());
			List<Unit> list = unitDAO.findAll();
			return list;
		}
	}
	
	@Override
	public Cashbox findCashbox(Long id) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection.open());
			return cashboxDAO.findOne(id);
		}
	}
	
	@Override
	public PaymentType findPaymentType(Long id) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			PaymentTypeDAO paymentTypeDAO = Application.getDAOFactory().getPaymentTypeDAO(connection.open());
			return paymentTypeDAO.findOne(id);
		}
	}
	
	@Override
	public List<LocaleRecord> getLocaleList() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			LocaleDAO localeDAO = Application.getDAOFactory().getLocaleDAO(connection.open());
			
			List<LocaleRecord> list = localeDAO.findAll();
			
			return list;
		}
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
		try (ConnectionWrapper connection = Application.getConnection()) {
			CompanyDAO companyDAO = Application.getDAOFactory().getCompanyDAO(connection.open());
			return companyDAO.findOne(id);
		}
	}
	
	@Override
	public List<PaymentType> getPaymentTypes() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			PaymentTypeDAO paymentTypeDAO = Application.getDAOFactory().getPaymentTypeDAO(connection.open());
			
			List<PaymentType> list = paymentTypeDAO.findAll();
			
			return list;
		}
	}
	
	@Override
	public List<Cashbox> getCashboxList() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection.open());
			
			List<Cashbox> list = cashboxDAO.findAll();
			
			return list;
		}
	}
}
