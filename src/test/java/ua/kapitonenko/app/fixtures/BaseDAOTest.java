package ua.kapitonenko.app.fixtures;

import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAOTest {
	protected static final Long USER_ID = 1L;
	protected static final Long UNIT_1 = 1L;
	protected static final Long UNIT_2 = 2L;
	protected static final Long TAX_1 = 1L;
	protected static final Long TAX_2 = 2L;
	protected static final Long PRODUCT_ID = 1L;
	protected static final Long USER_ROLE = 1L;
	protected static final Long PAYMENT = 1L;
	protected static final Long CASHBOX = 1L;
	protected static final Long RECEIPT_TYPE = 1L;
	protected static final Long RECEIPT_ID = 1L;
	
	protected Connection connection;
	
	protected abstract String getTableName();
	
	@Before
	public void setUp() throws Exception {
		connection = TestConnection.getInstance().getConnection();
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