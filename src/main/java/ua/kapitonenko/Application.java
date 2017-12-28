package ua.kapitonenko;

import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.connection.DataSourceConnectionPool;
import ua.kapitonenko.dao.interfaces.DAOFactory;
import ua.kapitonenko.dao.mysql.MysqlDaoFactory;
import ua.kapitonenko.service.ServiceFactory;
import ua.kapitonenko.service.impl.ServiceFactoryImpl;

public class Application {
	
	public static String defaultLocale = "en_US";
	public static String messageBundle = "messages";
	public static String settingsBundle = "settings";
	
	public static DAOFactory getDAOFactory() {
		return new MysqlDaoFactory();
	}
	
	public static ServiceFactory getServiceFactory() {
		return new ServiceFactoryImpl();
	}
	
	public static ConnectionPool getConnectionPool() {
		return DataSourceConnectionPool.getInstance();
	}
}
