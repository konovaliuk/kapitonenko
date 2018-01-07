package ua.kapitonenko.app.dao.mysql;


import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.UserRoleDAO;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.UserRoleTable;
import ua.kapitonenko.app.domain.records.UserRole;

import java.sql.Connection;

public class MysqlUserRoleDAO extends BaseLocalizedDAO<UserRole> implements UserRoleDAO {
	
	MysqlUserRoleDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return UserRoleTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<UserRole> getResultSetExtractor() {
		return rs -> {
			UserRole row = new UserRole();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			row.setBundleName(rs.getString(BaseTable.BUNDLE_NAME));
			row.setBundleKey(rs.getString(BaseTable.BUNDLE_KEY));
			return row;
		};
	}
}