package ua.kapitonenko.app.dao.mysql;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.LocaleDAO;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.LocaleTable;
import ua.kapitonenko.app.domain.records.LocaleRecord;

import java.sql.Connection;

public class MysqlLocaleDAO extends BaseDAO<LocaleRecord> implements LocaleDAO {
	private static final Logger LOGGER = Logger.getLogger(MysqlLocaleDAO.class);
	
	public MysqlLocaleDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return LocaleTable.NAME;
	}
	
	protected String getSelectAllQuery() {
		return "SELECT * FROM " + getTableName() + " ORDER BY " + LocaleTable.COLUMN_NAME;
	}
	
	protected String getSelectOneQuery() {
		return super.getSelectAllQuery() + WHERE_ID;
	}
	
	@Override
	protected ResultSetExtractor<LocaleRecord> getResultSetExtractor() {
		return rs -> {
			LocaleRecord row = new LocaleRecord();
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
	protected PreparedStatementSetter getInsertStatementSetter(LocaleRecord entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(LocaleRecord entity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected String getUpdateQuery() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean delete(LocaleRecord entity, Long userId) {
		throw new UnsupportedOperationException();
	}
}
