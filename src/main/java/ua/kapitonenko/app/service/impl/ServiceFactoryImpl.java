package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.service.*;

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
	
	@Override
	public ReportService getReportService() {
		return ReportServiceImpl.getInstance();
	}
}
