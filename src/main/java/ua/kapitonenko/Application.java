package ua.kapitonenko;

import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.connection.DataSourceConnectionPool;
import ua.kapitonenko.dao.interfaces.DAOFactory;
import ua.kapitonenko.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.service.ServiceFactory;
import ua.kapitonenko.service.impl.ServiceFactoryImpl;

import java.util.HashMap;
import java.util.Map;

public class Application {
	
	public static final String DEFAULT_LOCALE = "default.locale";
	public static final String MESSAGE_BUNDLE = "messages";
	public static final String SETTINGS_BUNDLE = "settings";
	public static final String ENCODING = "encoding";
	
	public static final String RECEIPT_TYPE_FISCAL = "receipt.fiscal";
	public static final String RECEIPT_TYPE_RETURN = "receipt.return";
	public static final String PAYMENT_TYPE_UNDEFINED = "payment.undefined";
	public static final String COMPANY = "company";
	
	private static Map<String, Long> ids = new HashMap<>();
	private static Map<String, String> params = new HashMap<>();
	
	static {
		ids.put(RECEIPT_TYPE_FISCAL, 1L);
		ids.put(RECEIPT_TYPE_RETURN, 2L);
		ids.put(PAYMENT_TYPE_UNDEFINED, 1L);
		ids.put(COMPANY, 1L);
		ids.put(DEFAULT_LOCALE, 1L);
		
		params.put(MESSAGE_BUNDLE, "messages");
		params.put(SETTINGS_BUNDLE, "settings");
		params.put(DEFAULT_LOCALE, "en_US");
		params.put(ENCODING, "UTF-8");
	}
	
	public static DAOFactory getDAOFactory() {
		return new MysqlDaoFactory();
	}
	
	public static ServiceFactory getServiceFactory() {
		return new ServiceFactoryImpl();
	}
	
	public static ConnectionPool getConnectionPool() {
		return DataSourceConnectionPool.getInstance();
	}
	
	public static Long getId(String key) {
		return ids.get(key);
	}
	
	public static String getParam(String key) {
		return params.get(key);
	}
	
	public static boolean isAutoActivationEnabled() {
		return true;
	}
}
