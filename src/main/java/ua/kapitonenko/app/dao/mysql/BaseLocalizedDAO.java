package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.domain.records.BaseLocalizedEntity;

import java.sql.Connection;

public abstract class BaseLocalizedDAO<E extends BaseLocalizedEntity> extends BaseDAO<E> {
	
	BaseLocalizedDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getInsertQuery() {
		return "INSERT INTO " + getTableName() + " (" +
				       BaseTable.COLUMN_NAME + ", " +
				       BaseTable.BUNDLE_NAME + ", " +
				       BaseTable.BUNDLE_KEY + ") " +
				       "VALUES (?, ?, ? )";
	}
	
	@Override
	protected String getUpdateQuery() {
		return "UPDATE " + getTableName() + " SET " +
				       BaseTable.COLUMN_NAME + " = ?, " +
				       BaseTable.BUNDLE_NAME + " = ?, " +
				       BaseTable.BUNDLE_KEY + " = ?" +
				       WHERE_ID;
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(E entity) {
		return ps -> {
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKey());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(E entity) {
		return ps -> {
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKey());
			ps.setLong(4, entity.getId());
		};
	}
	
	@Override
	public boolean delete(E entity, Long userId) {
		throw new UnsupportedOperationException();
	}
}