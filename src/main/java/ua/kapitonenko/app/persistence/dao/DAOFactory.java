package ua.kapitonenko.app.persistence.dao;


import ua.kapitonenko.app.persistence.connection.ConnectionWrapper;

import java.sql.Connection;

public interface DAOFactory {
	
	CompanyDAO getCompanyDAO(Connection connection);
	
	CashboxDAO getCashboxDao(Connection connection);
	
	PaymentTypeDAO getPaymentTypeDAO(Connection connection);
	
	ProductLocaleDAO getProductLocaleDAO(Connection connection);
	
	ProductDAO getProductDAO(Connection connection);
	
	ReceiptProductDAO getReceiptProductDAO(Connection connection);
	
	ReceiptTypeDAO getReceiptTypeDAO(Connection connection);
	
	ReceiptDAO getReceiptDAO(Connection connection);
	
	TaxCategoryDAO getTaxCategoryDAO(Connection connection);
	
	UnitDAO getUnitDAO(Connection connection);
	
	UserRoleDAO getUserRoleDAO(Connection connection);
	
	UserDAO getUserDAO(Connection connection);
	
	ZReportDAO getZReportDAO(Connection connection);
	
	LocaleDAO getLocaleDAO(Connection connection);
	
	ConnectionWrapper getConnection();
}