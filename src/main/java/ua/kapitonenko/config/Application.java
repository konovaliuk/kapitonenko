package ua.kapitonenko.config;

import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.connection.DataSourceConnectionPool;
import ua.kapitonenko.dao.interfaces.DAOFactory;
import ua.kapitonenko.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.service.ServiceFactory;
import ua.kapitonenko.service.impl.ServiceFactoryImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
	
	public static final String ROLE_CASHIER = "role.cashier";
	
	public static final String DEFAULT_LOCALE = "default.locale";
	public static final String MESSAGE_BUNDLE = "messages";
	public static final String SETTINGS_BUNDLE = "settings";
	public static final String ENCODING = "encoding";
	
	public static final String RECEIPT_TYPE_FISCAL = "receipt.fiscal";
	public static final String RECEIPT_TYPE_RETURN = "receipt.return";
	public static final String PAYMENT_TYPE_UNDEFINED = "payment.undefined";
	public static final String COMPANY = "company";
	public static final String ROLE_SENIOR = "role.senior";
	public static final String ROLE_MERCHANDISER = "role.merchandiser";
	private static final int RECORDS_PER_PAGE = 5;
	private static final String ROLE_GUEST = "role.guest";
	
	private static Map<String, Long> ids = new HashMap<>();
	private static Map<String, String> params = new HashMap<>();
	private static Map<Long, List<String>> access = new HashMap<>();

	
	static {
		ids.put(RECEIPT_TYPE_FISCAL, 1L);
		ids.put(RECEIPT_TYPE_RETURN, 2L);
		ids.put(PAYMENT_TYPE_UNDEFINED, 1L);
		ids.put(COMPANY, 1L);
		ids.put(DEFAULT_LOCALE, 1L);
		ids.put(ROLE_CASHIER, 1L);
		ids.put(ROLE_SENIOR, 2L);
		ids.put(ROLE_MERCHANDISER, 3L);
		ids.put(ROLE_GUEST, 4L);
		
		params.put(MESSAGE_BUNDLE, "messages");
		params.put(SETTINGS_BUNDLE, "settings");
		params.put(DEFAULT_LOCALE, "en_US");
		params.put(ENCODING, "UTF-8");
		
		access.put(
				getId(ROLE_GUEST), Arrays.asList(
						Routes.LOGIN,
						Routes.SIGNUP,
						Routes.LANGUAGE,
						Routes.HOME));
		
		access.put(
				getId(ROLE_CASHIER), Arrays.asList(
						Routes.LANGUAGE,
						Routes.PRODUCTS,
						Routes.RECEIPTS,
						Routes.RECEIPT_CREATE,
						Routes.RECEIPT_CANCEL,
						Routes.LOGOUT,
						Routes.HOME));
		
		access.put(
				getId(ROLE_MERCHANDISER), Arrays.asList(
						Routes.LANGUAGE,
						Routes.PRODUCTS,
						Routes.PRODUCTS_ADD,
						Routes.PRODUCTS_DELETE,
						Routes.PRODUCTS_CREATE,
						Routes.LOGOUT,
						Routes.HOME));
		
		access.put(
				getId(ROLE_SENIOR), Arrays.asList(
						Routes.LANGUAGE,
						Routes.PRODUCTS,
						Routes.REPORTS,
						Routes.REPORT_CREATE,
						Routes.REPORT_VIEW,
						Routes.RECEIPT_CANCEL,
						Routes.RECEIPT_RETURN,
						Routes.RECEIPTS,
						Routes.LOGOUT,
						Routes.HOME));
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
	
	public static int recordsPerPage() {
		return RECORDS_PER_PAGE;
	}
	
	public static boolean allowed(Long roleId, String route) {
		return access.get(roleId).contains(route);
	}
	
	public static boolean guestAllowed(String route) {
		return access.get(getId(ROLE_GUEST)).contains(route);
	}
}
