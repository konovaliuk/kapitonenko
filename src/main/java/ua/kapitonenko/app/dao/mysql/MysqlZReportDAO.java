package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.records.ZReport;

import java.sql.Connection;

public class MysqlZReportDAO extends BaseDAO<ZReport> implements ZReportDAO {
	private static final String UPDATE = "UPDATE " +
			                                     ZReportsTable.NAME + " SET " +
			                                     ZReportsTable.CASHBOX_ID + " = ?, " +
			                                     ZReportsTable.CASH_BALANCE + " = ? " +
			                                     WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     ZReportsTable.NAME + " ( " +
			                                     ZReportsTable.CASHBOX_ID + ", " +
			                                     ZReportsTable.CASH_BALANCE + ", " +
			                                     ZReportsTable.CREATED_AT + ", " +
			                                     ZReportsTable.CREATED_BY +
			                                     ") VALUES (?, ?, NOW(), ?)";
	
	MysqlZReportDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ZReportsTable.NAME;
	}
	
	@Override
	protected String getInsertQuery() {
		return INSERT;
	}
	
	@Override
	protected String getUpdateQuery() {
		return UPDATE;
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(final ZReport entity) {
		return ps -> {
			ps.setLong(1, entity.getCashboxId());
			ps.setBigDecimal(2, entity.getCashBalance());
			ps.setLong(3, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ZReport entity) {
		return ps -> {
			ps.setLong(1, entity.getCashboxId());
			ps.setBigDecimal(2, entity.getCashBalance());
			ps.setLong(3, entity.getId());
		};
	}
	
	@Override
	public boolean delete(ZReport entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected ResultSetExtractor<ZReport> getResultSetExtractor() {
		return rs -> {
			ZReport row = new ZReport();
			row.setId(rs.getLong(ZReportsTable.ID));
			row.setCashboxId(rs.getLong(ZReportsTable.CASHBOX_ID));
			row.setCashBalance(rs.getBigDecimal(ZReportsTable.CASH_BALANCE));
			row.setCreatedAt(rs.getTimestamp(ZReportsTable.CREATED_AT));
			row.setCreatedBy(rs.getLong(ZReportsTable.CREATED_BY));
			return row;
		};
	}
}