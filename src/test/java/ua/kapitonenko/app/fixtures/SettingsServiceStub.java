package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.persistence.dao.DAOFactory;
import ua.kapitonenko.app.persistence.records.*;
import ua.kapitonenko.app.service.SettingsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsServiceStub implements SettingsService {
	
	@Override
	public List<String> getSupportedLanguages() {
		return Arrays.asList("en", "uk");
	}
	
	@Override
	public Map<String, LocaleRecord> getSupportedLocales() {
		Map<String, LocaleRecord> map = new HashMap<>();
		map.put("en", new LocaleRecord(1L, "en_US"));
		map.put("uk", new LocaleRecord(2L, "uk_UA"));
		return map;
	}
	
	@Override
	public List<TaxCategory> getTaxCatList() {
		return Arrays.asList(new TaxCategory(), new TaxCategory(), new TaxCategory());
	}
	
	@Override
	public void clearCache() {
	
	}
	
	@Override
	public List<UserRole> getRoleList() {
		return null;
	}
	
	@Override
	public List<Unit> getUnitList() {
		return null;
	}
	
	@Override
	public Cashbox findCashbox(Long id) {
		return new Cashbox();
	}
	
	
	@Override
	public List<LocaleRecord> getLocaleList() {
		return Arrays.asList(new LocaleRecord(), new LocaleRecord());
	}
	
	@Override
	public Company findCompany(Long id) {
		return null;
	}
	
	@Override
	public List<PaymentType> getPaymentTypes() {
		return null;
	}
	
	@Override
	public List<Cashbox> getCashboxList() {
		return null;
	}
	
	@Override
	public PaymentType findPaymentType(Long paymentId) {
		return null;
	}
	
	@Override
	public void setDaoFactory(DAOFactory daoFactory) {
	
	}
}
