package ua.kapitonenko.app.dao.connection;

import java.sql.Connection;

public interface ConnectionPool {
	
	Connection getConnection();
	
	void close(Connection connection);
	
	
}
