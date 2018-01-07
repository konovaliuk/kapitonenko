package ua.kapitonenko.app.connection;

import java.sql.Connection;

public interface ConnectionPool {
	
	Connection getConnection();
	
	void close(Connection connection);
	
	
}
