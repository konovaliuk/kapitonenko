package ua.kapitonenko.app.config;

import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.connection.DataSourceConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.impl.ServiceFactoryImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
	
	public static final String DEFAULT_LOCALE = "default.locale";
	public static final String MESSAGE_BUNDLE = "messages";
	public static final String SETTINGS_BUNDLE = "settings";
	public static final String ENCODING = "encoding";
	
	public static final String RECEIPT_TYPE_FISCAL = "receipt.fiscal";
	public static final String RECEIPT_TYPE_RETURN = "receipt.return";
	public static final String PAYMENT_TYPE_UNDEFINED = "payment.undefined";
	public static final String PAYMENT_TYPE_CASH = "payment.cash";
	public static final String COMPANY = "company";
	
	public static final String ROLE_CASHIER = "role.cashier";
	public static final String ROLE_SENIOR = "role.senior";
	public static final String ROLE_MERCHANDISER = "role.merchandiser";
	private static final String ROLE_GUEST = "role.guest";
	
	private static final int RECORDS_PER_PAGE = 5;
	
	
	private static Map<String, Long> ids = new HashMap<>();
	private static Map<String, String> params = new HashMap<>();
	private static Map<Long, List<String>> access = new HashMap<>();
	
	
	static {
		ids.put(RECEIPT_TYPE_FISCAL, 1L);
		ids.put(RECEIPT_TYPE_RETURN, 2L);
		ids.put(PAYMENT_TYPE_UNDEFINED, 1L);
		ids.put(PAYMENT_TYPE_CASH, 2L);
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
						Actions.LOGIN,
						Actions.SIGNUP,
						Actions.LANGUAGE,
						Actions.HOME));
		
		access.put(
				getId(ROLE_CASHIER), Arrays.asList(
						Actions.LANGUAGE,
						Actions.PRODUCTS,
						Actions.RECEIPTS,
						Actions.RECEIPT_CREATE,
						Actions.RECEIPT_CANCEL,
						
						Actions.RECEIPT_EDIT_CANCEL,
						Actions.RECEIPT_EDIT_DELETE,
						Actions.RECEIPT_EDIT_SAVE,
						Actions.RECEIPT_EDIT_UPDATE,
						Actions.RECEIPT_EDIT_ADD,
						
						Actions.LOGOUT,
						Actions.HOME));
		
		access.put(
				getId(ROLE_MERCHANDISER), Arrays.asList(
						Actions.LANGUAGE,
						Actions.PRODUCTS,
						Actions.PRODUCTS_UPDATE,
						Actions.PRODUCTS_DELETE,
						Actions.PRODUCTS_CREATE,
						Actions.LOGOUT,
						Actions.HOME));
		
		access.put(
				getId(ROLE_SENIOR), Arrays.asList(
						Actions.LANGUAGE,
						Actions.REPORTS,
						Actions.REPORT_CREATE,
						Actions.REPORT_VIEW,
						Actions.RECEIPT_CANCEL,
						Actions.RECEIPT_RETURN,
						Actions.RECEIPTS,
						
						Actions.RECEIPT_EDIT_CANCEL,
						Actions.RECEIPT_EDIT_DELETE,
						Actions.RECEIPT_EDIT_SAVE,
						Actions.RECEIPT_EDIT_UPDATE,
						Actions.RECEIPT_EDIT_ADD,
						
						Actions.LOGOUT,
						Actions.HOME));
	}
	
	public static DAOFactory getDAOFactory() {
		return MysqlDaoFactory.getInstance();
	}
	
	public static ServiceFactory getServiceFactory() {
		return ServiceFactoryImpl.getInstance();
	}
	
	public static ConnectionWrapper getConnection() {
		return new DataSourceConnectionWrapper();
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
	
	public static boolean allowed(Long roleId, String action) {
		return access.get(roleId).contains(action);
	}
	
	public static boolean guestAllowed(String action) {
		return access.get(getId(ROLE_GUEST)).contains(action);
	}
}
