package ua.kapitonenko.app.dao.mysql;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;

import java.sql.Connection;

public class MysqlDaoFactory implements DAOFactory {
	
	private static final Logger LOGGER = Logger.getLogger(MysqlDaoFactory.class);
	
	private static MysqlDaoFactory instance = new MysqlDaoFactory();
	
	private MysqlDaoFactory() {
	}
	
	public static MysqlDaoFactory getInstance() {
		return instance;
	}
	
	@Override
	public CompanyDAO getCompanyDAO(Connection connection) {
		return new MysqlCompanyDAO(connection);
	}
	
	@Override
	public CashboxDAO getCashboxDao(Connection connection) {
		return new MysqlCashboxDAO(connection);
	}
	
	@Override
	public PaymentTypeDAO getPaymentTypeDAO(Connection connection) {
		return new MysqlPaymentTypeDAO(connection);
	}
	
	@Override
	public ProductLocaleDAO getProductLocaleDAO(Connection connection) {
		return new MysqlProductLocaleDAO(connection);
	}
	
	@Override
	public ProductDAO getProductDAO(Connection connection) {
		return new MysqlProductDAO(connection);
	}
	
	@Override
	public ReceiptProductDAO getReceiptProductDAO(Connection connection) {
		return new MysqlReceiptProductDAO(connection);
	}
	
	@Override
	public ReceiptTypeDAO getReceiptTypeDAO(Connection connection) {
		return new MysqlReceiptTypeDAO(connection);
	}
	
	@Override
	public ReceiptDAO getReceiptDAO(Connection connection) {
		return new MysqlReceiptDAO(connection);
	}
	
	@Override
	public TaxCategoryDAO getTaxCategoryDAO(Connection connection) {
		return new MysqlTaxCategoryDAO(connection);
	}
	
	@Override
	public UnitDAO getUnitDAO(Connection connection) {
		return new MysqlUnitDAO(connection);
	}
	
	@Override
	public UserRoleDAO getUserRoleDAO(Connection connection) {
		return new MysqlUserRoleDAO(connection);
	}
	
	@Override
	public UserDAO getUserDAO(Connection connection) {
		return new MysqlUserDAO(connection);
	}
	
	@Override
	public ZReportDAO getZReportDAO(Connection connection) {
		return new MysqlZReportDAO(connection);
	}
	
	@Override
	public LocaleDAO getLocaleDAO(Connection connection) {
		return new MysqlLocaleDAO(connection);
	}
	
	@Override
	public ConnectionWrapper getConnection() {
		return Application.getConnection();
	}
}