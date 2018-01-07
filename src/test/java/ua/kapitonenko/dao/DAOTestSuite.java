package ua.kapitonenko.dao;

import fixtures.TestConnectionPool;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(Suite.class)

@Suite.SuiteClasses({
		                    CashboxDAOTest.class,
		                    CompanyDAOTest.class,
		                    PaymentTypeDAOTest.class,
		                    ProductDAOTest.class,
		                    ProductLocaleDAOTest.class,
		                    ReceiptDAOTest.class,
		                    ReceiptProductDAOTest.class,
		                    ReceiptTypeDAOTest.class,
		                    TaxCategoryDAOTest.class,
		                    UnitDAOTest.class,
		                    UserDAOTest.class,
		                    UserRoleDAOTest.class,
		                    ZReportDAOTest.class
})
public class DAOTestSuite {
	@BeforeClass
	public static void setUp() throws Exception {
		
		Connection connection = TestConnectionPool.getInstance().getConnection();
		connection.setAutoCommit(false);
		try (Statement statement = connection.createStatement()) {
			statement.execute("SET FOREIGN_KEY_CHECKS = 0;\n" +
					                  "TRUNCATE `user_roles`;\n" +
					                  "TRUNCATE `users`;\n" +
					                  "TRUNCATE `cashboxes`;\n" +
					                  "TRUNCATE `units`;\n" +
					                  "TRUNCATE `tax_categories`;\n" +
					                  "TRUNCATE `locale`;\n" +
					                  "INSERT INTO `user_roles` (`name`, `bundle_name`, `bundle_key`) VALUES ('admin', 'messages', 'role.admin');\n" +
					                  "INSERT INTO `users` (`user_role_id`, `username`, `password_hash`, `active`, `created_at`)\n" +
					                  "VALUES (1, 'admin', 'admin', TRUE, NOW());\n" +
					                  "INSERT INTO `cashboxes` (`fn_number`, `zn_number`, `make`)\n" +
					                  "VALUES ('default', 'default', 'default'), ('1010101010', 'AT2020202020', 'КРОХА'),\n" +
					                  "  ('0123456789', '12345678910', 'Datecs');\n" +
					                  "INSERT INTO `units` (`name`, `bundle_name`, `bundle_key`)\n" +
					                  "VALUES ('kilogram', 'settings', 'unit.kg'), ('piece', 'settings', 'pc');\n" +
					                  "INSERT INTO `tax_categories` (`name`, `bundle_name`, `bundle_key`)\n" +
					                  "VALUES ('1', 'settings', 'tax.1'), ('2', 'settings', 'tax.2');\n" +
					                  "INSERT INTO `locale` (`name`) VALUES ('en_US'), ('uk_UA');\n" +
					                  "INSERT INTO `receipt_types` (`id`, `name`, `bundle_name`, `bundle_key`)\n" +
					                  "VALUES (1, 'income', 'settings', 'receipt.type.income'), (2, 'return', 'settings', 'receipt.type.return');\n" +
					                  "INSERT INTO `payment_types` (`name`, `bundle_name`, `bundle_key`) VALUES ('cash', 'settings', 'payment.type.cash');\n" +
					                  "INSERT INTO `products` (`id`, `unit_id`, `price`, `tax_category_id`, `quantity`, `created_at`, `created_by`, `deleted_at`, `deleted_by`)\n" +
					                  "VALUES (1, 1, 9.99, 1, 999.999, NOW(), 1, NULL, NULL);\n" +
					                  "SET FOREIGN_KEY_CHECKS = 1;  \n");
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
		} finally {
			TestConnectionPool.getInstance().close(connection);
		}
	}
}
