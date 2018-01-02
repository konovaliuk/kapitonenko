package ua.kapitonenko.dao.mysql;

import org.apache.log4j.Logger;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.PaymentTypeDAO;
import ua.kapitonenko.dao.tables.BaseTable;
import ua.kapitonenko.dao.tables.PaymentTypesTable;
import ua.kapitonenko.domain.entities.PaymentType;

import java.sql.Connection;

public class MysqlPaymentTypeDAO extends BaseLocalizedDAO<PaymentType> implements PaymentTypeDAO {
	private static final Logger LOGGER = Logger.getLogger(MysqlPaymentTypeDAO.class);
	
	
	MysqlPaymentTypeDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getSelectAllQuery() {
		return super.getSelectAllQuery() + " WHERE " + BaseTable.COLUMN_NAME + " <> 'undefined'";
	}
	
	protected String getSelectOneQuery() {
		return super.getSelectAllQuery() + WHERE_ID;
	}
	
	@Override
	protected String getTableName() {
		return PaymentTypesTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<PaymentType> getResultSetExtractor() {
		return rs -> {
			PaymentType row = new PaymentType();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			row.setBundleName(rs.getString(BaseTable.BUNDLE_NAME));
			row.setBundleKey(rs.getString(BaseTable.BUNDLE_KEY));
			return row;
		};
	}
}