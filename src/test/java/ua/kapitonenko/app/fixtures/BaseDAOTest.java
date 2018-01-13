package ua.kapitonenko.app.fixtures;

import org.junit.Before;

import java.sql.Connection;

public abstract class BaseDAOTest {
	protected static final Long USER_ID = 1L;
	protected static final Long UNIT = 1L;
	protected static final Long TAX_1 = 1L;
	protected static final Long TAX_2 = 2L;
	protected static final Long PRODUCT_ID = 1L;
	protected static final Long USER_ROLE = 1L;
	protected static final Long PAYMENT = 1L;
	protected static final Long CASHBOX_1 = 1L;
	protected static final Long CASHBOX_2 = 2L;
	protected static final Long RECEIPT_TYPE = 1L;
	protected static final Long RECEIPT_ID = 1L;
	
	protected Connection connection;
	
	@Before
	public void setUp() throws Exception {
		connection = TestConnection.getInstance().getConnection();
	}
}