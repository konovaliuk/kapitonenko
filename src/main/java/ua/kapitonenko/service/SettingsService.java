package ua.kapitonenko.service;

import ua.kapitonenko.domain.Cashbox;

import java.util.Map;

public interface SettingsService {
	Map<Long, String> getRolesMap();
	
	Cashbox findCashbox(Long id);
	
	Map<String, String> getLocaleMap();
}
