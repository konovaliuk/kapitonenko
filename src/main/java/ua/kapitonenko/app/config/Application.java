package ua.kapitonenko.app.config;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.connection.DataSourceConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.app.domain.records.Company;
import ua.kapitonenko.app.exceptions.AppException;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.impl.ServiceFactoryImpl;

import javax.servlet.ServletContext;

public class Application {
	private static final Logger LOGGER = Logger.getLogger(Application.class);
	private static final int RECORDS_PER_PAGE = 5;
	private static final boolean AUTO_ACTIVATION = true;
	
	private static DAOFactory daoFactory = MysqlDaoFactory.getInstance();
	private static ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
	private static Class<? extends ConnectionWrapper> connectionClass = DataSourceConnectionWrapper.class;
	
	public static DAOFactory getDAOFactory() {
		return daoFactory;
	}
	
	public static void setDAOFactory(DAOFactory impl) {
		daoFactory = impl;
	}
	
	public static ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	public static void setServiceFactory(ServiceFactory impl) {
		serviceFactory = impl;
	}
	
	public static <E extends ConnectionWrapper> void setConnectionClass(Class<E> className) {
		connectionClass = className;
	}
	
	public static ConnectionWrapper getConnection() {
		try {
			return connectionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AppException(e);
		}
	}
	
	public static boolean isAutoActivationEnabled() {
		return AUTO_ACTIVATION;
	}
	
	public static void init(ServletContext context) {
		LOGGER.debug("application init");
		SettingsService settingsService = Application.getServiceFactory().getSettingsService();
		Company company = settingsService.findCompany(Ids.COMPANY.getValue());
		
		context.setAttribute(Keys.COMPANY, company);
		context.setAttribute(Params.MESSAGE_BUNDLE.getValue(), Params.MESSAGE_BUNDLE.getValue());
		context.setAttribute(Params.SETTINGS_BUNDLE.getValue(), Params.SETTINGS_BUNDLE.getValue());
		context.setAttribute(Keys.LANGUAGES, settingsService.getSupportedLanguages());
	}
	
	public enum Ids {
		RECEIPT_TYPE_FISCAL(1L),
		RECEIPT_TYPE_RETURN(2L),
		PAYMENT_TYPE_UNDEFINED(1L),
		PAYMENT_TYPE_CASH(2L),
		COMPANY(1L),
		DEFAULT_LOCALE(1L),
		ROLE_CASHIER(1L),
		ROLE_SENIOR(2L),
		ROLE_MERCHANDISER(3L),
		ROLE_GUEST(4L);
		
		private Long value;
		
		Ids(Long value) {
			this.value = value;
		}
		
		public Long getValue() {
			return value;
		}
	}
	
	public static int recordsPerPage() {
		return RECORDS_PER_PAGE;
	}
	
	public enum Params {
		
		MESSAGE_BUNDLE("messages"),
		SETTINGS_BUNDLE("settings"),
		DEFAULT_LOCALE("en_US"),
		ENCODING("UTF-8");
		
		private String value;
		
		Params(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
}
