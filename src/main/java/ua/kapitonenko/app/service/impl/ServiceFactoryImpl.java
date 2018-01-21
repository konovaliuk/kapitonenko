package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.service.*;

import java.util.HashMap;

public class ServiceFactoryImpl implements ServiceFactory {
	
	private static ServiceFactoryImpl instance = new ServiceFactoryImpl();
	
	private HashMap<ServiceName, Service> services = new HashMap<>();
	
	private ServiceFactoryImpl() {
		initServices();
	}
	
	public static ServiceFactoryImpl getInstance() {
		return instance;
	}
	
	private void initServices() {
		services.put(ServiceName.USER, new UserServiceImpl());
		services.put(ServiceName.PRODUCT, new ProductServiceImpl());
		services.put(ServiceName.SETTINGS, new SettingsServiceImpl());
		services.put(ServiceName.RECEIPT, new ReceiptServiceImpl());
		services.put(ServiceName.REPORT, new ReportServiceImpl());
	}
	
	@Override
	public UserService getUserService() {
		return (UserService) services.get(ServiceName.USER);
	}
	
	@Override
	public SettingsService getSettingsService() {
		return (SettingsService) services.get(ServiceName.SETTINGS);
	}
	
	@Override
	public ProductService getProductService() {
		return (ProductService) services.get(ServiceName.PRODUCT);
	}
	
	@Override
	public ReceiptService getReceiptService() {
		return (ReceiptService) services.get(ServiceName.RECEIPT);
	}
	
	@Override
	public ReportService getReportService() {
		return (ReportService) services.get(ServiceName.REPORT);
	}
}
