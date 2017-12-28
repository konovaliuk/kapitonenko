package fixtures;

import ua.kapitonenko.Exceptions.DAOException;
import ua.kapitonenko.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionPool implements ConnectionPool {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String URL = "jdbc:mysql://localhost:3366/cashregister_test?autoReconnect=true&useSSL=false";
	
	private static TestConnectionPool instance = new TestConnectionPool();
	
	private TestConnectionPool() {
	}
	
	public static TestConnectionPool getInstance() {
		return instance;
	}
	
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
