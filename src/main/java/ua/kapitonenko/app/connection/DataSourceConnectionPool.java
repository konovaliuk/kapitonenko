package ua.kapitonenko.app.connection;

import ua.kapitonenko.app.exceptions.DAOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionPool implements ConnectionPool {
	private static final String RES_NAME = "java:/comp/env/jdbc/cashregister";
	private static DataSourceConnectionPool instance = new DataSourceConnectionPool();
	private static DataSource dataSource;
	
	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup(RES_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private DataSourceConnectionPool() {
	}
	
	public static DataSourceConnectionPool getInstance() {
		return instance;
	}
	
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	public void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
}
