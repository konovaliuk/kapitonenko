package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.LocaleDAO;
import ua.kapitonenko.dao.tables.BaseTable;
import ua.kapitonenko.dao.tables.LocaleTable;
import ua.kapitonenko.domain.Locale;

import java.sql.Connection;

public class MysqlLocaleDAO extends BaseDAO<Locale> implements LocaleDAO {
	
	public MysqlLocaleDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return LocaleTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<Locale> getResultSetExtractor() {
		return rs -> {
			Locale row = new Locale();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			return row;
		};
	}
	
	@Override
	protected String getInsertQuery() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(Locale entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(Locale entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected String getUpdateQuery() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean delete(Locale entity, Long userId) {
		throw new UnsupportedOperationException();
	}
}
