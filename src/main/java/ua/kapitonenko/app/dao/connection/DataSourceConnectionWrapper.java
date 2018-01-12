package ua.kapitonenko.app.dao.connection;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.exceptions.DAOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionWrapper implements ConnectionWrapper {
	
	private static final Logger LOGGER = Logger.getLogger(DataSourceConnectionWrapper.class);
	private static final String RES_NAME = "java:/comp/env/jdbc/cashregister";
	private static DataSource dataSource;
	
	static {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup(RES_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private Connection connection;
	private boolean autoCommit = true;
	private boolean committed = false;
	
	@Override
	public Connection open() {
		if (connection == null) {
			try {
				connection = dataSource.getConnection();
				LOGGER.debug(connection);
			} catch (SQLException e) {
				throw new DAOException(e);
			}
		}
		return connection;
	}
	
	@Override
	public void beginTransaction() {
		try {
			open();
			connection.setAutoCommit(false);
			autoCommit = false;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public void commit() {
		try {
			connection.commit();
			connection.setAutoCommit(true);
			committed = true;
			autoCommit = true;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public void rollback() {
		try {
			connection.rollback();
			connection.setAutoCommit(true);
			autoCommit = true;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	public void close() {
		try {
			if (!autoCommit && !committed) {
				rollback();
			}
			connection.close();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
}
