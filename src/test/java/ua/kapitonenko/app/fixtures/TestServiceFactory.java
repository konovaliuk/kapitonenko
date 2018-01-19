package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.service.*;

import java.util.HashMap;

public class TestServiceFactory implements ServiceFactory {
	
	private static TestServiceFactory instance = new TestServiceFactory();
	
	private HashMap<ServiceName, Service> services = new HashMap<>();
	
	private TestServiceFactory() {
		initServices();
	}
	
	public static TestServiceFactory getInstance() {
		return instance;
	}
	
	private void initServices() {
		services.put(ServiceName.USER, null);
		services.put(ServiceName.PRODUCT, new ProductServiceStub());
		services.put(ServiceName.SETTINGS, new SettingsServiceStub());
		services.put(ServiceName.RECEIPT, new ReceiptServiceStub());
		services.put(ServiceName.REPORT, null);
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
