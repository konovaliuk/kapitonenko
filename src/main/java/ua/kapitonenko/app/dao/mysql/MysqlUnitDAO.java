package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.UnitDAO;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.UnitsTable;
import ua.kapitonenko.app.domain.records.Unit;

import java.sql.Connection;

public class MysqlUnitDAO extends BaseLocalizedDAO<Unit> implements UnitDAO {
	
	MysqlUnitDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return UnitsTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<Unit> getResultSetExtractor() {
		return rs -> {
			Unit row = new Unit();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			row.setBundleName(rs.getString(BaseTable.BUNDLE_NAME));
			row.setBundleKey(rs.getString(BaseTable.BUNDLE_KEY));
			return row;
		};
	}

}