package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.PaymentTypeDAO;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.PaymentTypesTable;

import java.sql.Connection;

public class MysqlPaymentTypeDAO extends BaseLocalizedDAO<PaymentType> implements PaymentTypeDAO {
	
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