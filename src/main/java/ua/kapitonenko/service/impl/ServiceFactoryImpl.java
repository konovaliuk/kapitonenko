package ua.kapitonenko.service.impl;

import ua.kapitonenko.service.*;

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
	
	@Override
	public ReceiptService getReceiptService() {
		return ReceiptServiceImpl.getInstance();
	}
}
