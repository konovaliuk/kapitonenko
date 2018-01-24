package ua.kapitonenko.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.impl.ModelFactoryImpl;
import ua.kapitonenko.app.exceptions.AppException;
import ua.kapitonenko.app.persistence.connection.ConnectionWrapper;
import ua.kapitonenko.app.persistence.connection.DataSourceConnectionWrapper;
import ua.kapitonenko.app.persistence.dao.DAOFactory;
import ua.kapitonenko.app.persistence.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.app.persistence.records.Company;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.impl.ServiceFactoryImpl;

import javax.servlet.ServletContext;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static java.lang.System.lineSeparator;

/**
 * {@code Application} is a global application configuration object.
 * Contains default configuration information on concrete implementations of factories,
 * settings params and ids of settings records on which the application logic is based
 * (later can be rewritten to read settings from properties files or database).
 * Provides methods to change default values.
 */
public class Application {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final int RECORDS_PER_PAGE = 5;
	
	private static DAOFactory daoFactory = MysqlDaoFactory.getInstance();
	private static ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();
	private static ModelFactory modelFactory = ModelFactoryImpl.getInstance();
	private static Class<? extends ConnectionWrapper> connectionClass = DataSourceConnectionWrapper.class;
	private static boolean autoActivation = true;
	
	private Application() {
	}
	
	/**
	 * Returns concrete implementation of {@code DAOFactory}.
	 */
	public static DAOFactory getDAOFactory() {
		return daoFactory;
	}
	
	/**
	 * Sets concrete implementation of {@code DAOFactory}.
	 */
	public static void setDAOFactory(DAOFactory impl) {
		daoFactory = impl;
		logger.info("Configuration changed: DAOFactory={}", impl);
	}
	
	/**
	 * Returns concrete implementation of {@code ServiceFactory}.
	 */
	public static ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	/**
	 * Sets concrete implementation of {@code ServiceFactory}.
	 */
	public static void setServiceFactory(ServiceFactory impl) {
		serviceFactory = impl;
		logger.info("Configuration changed: ServiceFactory={}", impl);
	}
	
	/**
	 * Returns concrete implementation of {@code ModelFactory}.
	 */
	public static ModelFactory getModelFactory() {
		return modelFactory;
	}
	
	/**
	 * Sets concrete implementation of {@code ModelFactory}.
	 */
	public static void setModelFactory(ModelFactory impl) {
		modelFactory = impl;
		logger.info("Configuration changed: ModelFactory={}", impl);
	}
	
	/**
	 * Sets the name of the class that implements {@code ConnectionWrapper}.
	 */
	public static <E extends ConnectionWrapper> void setConnectionClass(Class<E> className) {
		connectionClass = className;
		logger.info("Configuration changed: ConnectionWrapper={}", className);
	}
	
	/**
	 * Returns new instance of {@code ConnectionWrapper} implementation.
	 */
	public static ConnectionWrapper getConnection() {
		try {
			return connectionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AppException(e);
		}
	}
	
	/**
	 * Sets the value of AutoActivationMode.
	 */
	public static void setAutoActivationMode(boolean enabled) {
		autoActivation = enabled;
		logger.info("Configuration changed: autoActivation={}", autoActivation);
	}
	
	/**
	 * Gets the value of AutoActivationMode.
	 * It is used by {@link ua.kapitonenko.app.service.UserService} during creation of new user account.
	 * If true, service sets the {@link ua.kapitonenko.app.persistence.records.User} property "active" to true,
	 * otherwise to false.
	 * Can be used to provide control on registration. For example, admin panel can be implemented later,
	 * where admin can approve or decline new users.
	 *
	 */
	public static boolean isAutoActivationEnabled() {
		return autoActivation;
	}
	
	/**
	 * Saves basic application information used by jsp-pages in context attributes.
	 */
	public static void init(ServletContext context) {
		logger.info("Application initialized with configuration:{}    DAOFactory={},{}    ServiceFactory={},{}    ModelFactory={},{}    autoActivation={}",
				lineSeparator(), daoFactory.getClass(), lineSeparator(), serviceFactory.getClass(), lineSeparator(), modelFactory.getClass(), lineSeparator(), isAutoActivationEnabled());
		SettingsService settingsService = Application.getServiceFactory().getSettingsService();
		Company company = settingsService.findCompany(Ids.COMPANY.getValue());
		List<String> languages = settingsService.getSupportedLanguages();
		String messages = Params.MESSAGE_BUNDLE.getValue();
		
		context.setAttribute(Keys.COMPANY, company);
		context.setAttribute(messages, messages);
		context.setAttribute(Keys.LANGUAGES, languages);
		logger.info("Context attributes are set:{}    {},{}    message bundle={}, supported languages={}",
				lineSeparator(), company, lineSeparator(), messages, languages);
		
	}
	
	/**
	 * Enumerates ids of settings records on which the application logic is based.
	 */
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
	
	/**
	 * Returns default number of records per page
	 * used by {@link ua.kapitonenko.app.controller.helpers.PaginationHelper}.
	 */
	public static int recordsPerPage() {
		return RECORDS_PER_PAGE;
	}
	
	public enum Params {
		
		MESSAGE_BUNDLE("messages"),
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
