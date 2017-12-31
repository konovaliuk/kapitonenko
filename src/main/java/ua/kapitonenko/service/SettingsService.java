package ua.kapitonenko.service;

import ua.kapitonenko.domain.Cashbox;
import ua.kapitonenko.domain.Locale;
import ua.kapitonenko.domain.TaxCategory;
import ua.kapitonenko.domain.Unit;

import java.util.List;
import java.util.Map;

public interface SettingsService {
	Map<Long, String> getRolesMap();
	
	Map<Long, String> getTaxMap();
	
	List<TaxCategory> getTaxCatList();
	
	Map<Long, String> getUnitMap();
	
	List<Unit> getUnitList();
	
	Cashbox findCashbox(Long id);
	
	List<Locale> getLocaleList();
	
	List<String> getSupportedLanguages();
	
	Map<String, Locale> getSupportedLocales();
}
