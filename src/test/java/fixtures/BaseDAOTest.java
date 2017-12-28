package fixtures;

import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAOTest {
	protected static final Long USER_ID = 1L;
	
	protected Connection connection;
	
	protected abstract String getTableName();
	
	@Before
	public void setUp() throws Exception {
		connection = TestConnectionPool.getInstance().getConnection();
	}
	
	protected void truncateTable() throws SQLException {
		try (Statement stmt = connection.createStatement()) {
			
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 0");
			stmt.addBatch("TRUNCATE " + getTableName());
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 1");
			stmt.executeBatch();
		}
	}
}