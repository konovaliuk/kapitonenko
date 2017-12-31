package ua.kapitonenko.service.impl;

import ua.kapitonenko.service.ProductService;
import ua.kapitonenko.service.ServiceFactory;
import ua.kapitonenko.service.SettingsService;
import ua.kapitonenko.service.UserService;

public class ServiceFactoryImpl implements ServiceFactory {
	@Override
	public UserService getUserService() {
		return UserServiceImpl.getInstance();
	}
	
	@Override
	public SettingsService getSettingsService() {
		return SettingsServiceImpl.getInstance();
	}
	
	@Override
	public ProductService getProductService() {
		return ProductServiceImpl.getInstance();
	}
}
