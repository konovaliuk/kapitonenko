package ua.kapitonenko.app.persistence.dao.mysql;


import ua.kapitonenko.app.persistence.dao.UserRoleDAO;
import ua.kapitonenko.app.persistence.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.persistence.records.UserRole;
import ua.kapitonenko.app.persistence.tables.BaseTable;
import ua.kapitonenko.app.persistence.tables.UserRoleTable;

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