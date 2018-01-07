package ua.kapitonenko.service;

import ua.kapitonenko.domain.entities.*;

import java.util.List;
import java.util.Map;

public interface SettingsService {
	List<UserRole> getRoleList();
	
	Map<Long, String> getTaxMap();
	
	List<TaxCategory> getTaxCatList();
	
	Map<Long, String> getUnitMap();
	
	List<Unit> getUnitList();
	
	Cashbox findCashbox(Long id);
	
	PaymentType findPaymentType(Long id);
	
	List<Locale> getLocaleList();
	
	List<String> getSupportedLanguages();
	
	Map<String, Locale> getSupportedLocales();
	
	Company findCompany(Long id);
	
	List<PaymentType> getPaymentTypes();
	
	List<Cashbox> getCashboxList();
}
