package ua.kapitonenko.connection;

import java.sql.Connection;

public interface ConnectionPool {
	
	Connection getConnection();
	
	void close(Connection connection);
	
	
}
