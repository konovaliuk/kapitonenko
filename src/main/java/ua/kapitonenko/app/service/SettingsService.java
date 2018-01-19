package ua.kapitonenko.app.service;

import ua.kapitonenko.app.dao.records.*;

import java.util.List;
import java.util.Map;

public interface SettingsService extends Service {
	
	void clearCache();
	
	List<UserRole> getRoleList();
	
	List<TaxCategory> getTaxCatList();
	
	List<Unit> getUnitList();
	
	Cashbox findCashbox(Long id);
	
	List<LocaleRecord> getLocaleList();
	
	List<String> getSupportedLanguages();
	
	Map<String, LocaleRecord> getSupportedLocales();
	
	Company findCompany(Long id);
	
	List<PaymentType> getPaymentTypes();
	
	List<Cashbox> getCashboxList();
}
