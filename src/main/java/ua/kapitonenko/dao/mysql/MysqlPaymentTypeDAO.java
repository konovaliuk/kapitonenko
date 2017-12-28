package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.PaymentTypeDAO;
import ua.kapitonenko.dao.tables.BaseTable;
import ua.kapitonenko.dao.tables.PaymentTypesTable;
import ua.kapitonenko.domain.PaymentType;

import java.sql.Connection;

public class MysqlPaymentTypeDAO extends BaseLocalizedDAO<PaymentType> implements PaymentTypeDAO {
	
	MysqlPaymentTypeDAO(Connection connection) {
		super(connection);
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