package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.ReceiptTypeDAO;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.records.ReceiptType;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.ReceiptTypesTable;

import java.sql.Connection;

public class MysqlReceiptTypeDAO extends BaseLocalizedDAO<ReceiptType> implements ReceiptTypeDAO {
	
	MysqlReceiptTypeDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ReceiptTypesTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<ReceiptType> getResultSetExtractor() {
		return rs -> {
			ReceiptType row = new ReceiptType();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			row.setBundleName(rs.getString(BaseTable.BUNDLE_NAME));
			row.setBundleKey(rs.getString(BaseTable.BUNDLE_KEY));
			return row;
		};
	}
	
}