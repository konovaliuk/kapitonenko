package ua.kapitonenko.app.persistence.connection;

import java.sql.Connection;

public interface ConnectionWrapper extends AutoCloseable {
	
	Connection open();
	
	void beginTransaction();
	
	void commit();
	
	void rollback();
	
	@Override
	void close();
}
