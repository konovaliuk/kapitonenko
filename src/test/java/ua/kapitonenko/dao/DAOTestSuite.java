package ua.kapitonenko.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
		                    CashboxDAOTest.class,
		                    ProductDAOTest.class,
		                    ProductDAOTest.class,
		                    TaxCategoryDAOTest.class,
		                    UnitDAOTest.class,
		                    UserDAOTest.class,
		                    UserRoleDAOTest.class
})
public class DAOTestSuite {
}
