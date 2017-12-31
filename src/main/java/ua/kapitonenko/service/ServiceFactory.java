package ua.kapitonenko.service;

public interface ServiceFactory {
	UserService getUserService();
	
	SettingsService getSettingsService();
	
	ProductService getProductService();
}
