package ua.kapitonenko.app.service;

public interface ServiceFactory {
	
	UserService getUserService();
	
	SettingsService getSettingsService();
	
	ProductService getProductService();
	
	ReceiptService getReceiptService();
	
	ReportService getReportService();
	
	public enum ServiceName {
		USER, SETTINGS, PRODUCT, RECEIPT, REPORT
	}
}
