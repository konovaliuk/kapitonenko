package ua.kapitonenko.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.connection.DataSourceConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.app.dao.records.Company;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.impl.ModelFactoryImpl;
import ua.kapitonenko.app.exceptions.AppException;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.SettingsService;
import ua.kapitonenko.app.service.impl.ServiceFactoryImpl;

import javax.servlet.ServletContext;
import java.lang.invoke.MethodHandles;
import java.util.List;

import static java.lang.System.lineSeparator;

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
	
	public static DAOFactory getDAOFactory() {
		return daoFactory;
	}
	
	public static void setDAOFactory(DAOFactory impl) {
		daoFactory = impl;
		logger.info("Configuration changed: DAOFactory={}", impl);
	}
	
	public static ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	public static void setServiceFactory(ServiceFactory impl) {
		serviceFactory = impl;
		logger.info("Configuration changed: ServiceFactory={}", impl);
	}
	
	public static ModelFactory getModelFactory() {
		return modelFactory;
	}
	
	public static void setModelFactory(ModelFactory impl) {
		modelFactory = impl;
		logger.info("Configuration changed: ModelFactory={}", impl);
	}
	
	public static <E extends ConnectionWrapper> void setConnectionClass(Class<E> className) {
		connectionClass = className;
		logger.info("Configuration changed: ConnectionWrapper={}", className);
	}
	
	public static ConnectionWrapper getConnection() {
		try {
			return connectionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AppException(e);
		}
	}
	
	public static void setAutoActivationMode(boolean enabled) {
		autoActivation = enabled;
		logger.info("Configuration changed: autoActivation={}", autoActivation);
	}
	
	public static boolean isAutoActivationEnabled() {
		return autoActivation;
	}
	
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
