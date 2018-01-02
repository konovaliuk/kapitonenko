package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ZReportDAO;
import ua.kapitonenko.dao.tables.ZReportsTable;
import ua.kapitonenko.domain.entities.ZReport;

import java.sql.Connection;
import java.util.Date;

public class MysqlZReportDAO extends BaseDAO<ZReport> implements ZReportDAO {
	private static final String UPDATE = "" + WHERE_ID;
	private static final String INSERT = "";
	
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
			ps.setLong(1, entity.getMachineId());
			ps.setLong(2, entity.getLastReceiptId());
			ps.setString(3, entity.getCashBalance());
			ps.setTimestamp(4, new java.sql.Timestamp(entity.getCreatedAt().getTime()));
			ps.setLong(5, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ZReport entity) {
		return ps -> {
			ps.setLong(1, entity.getMachineId());
			ps.setLong(2, entity.getLastReceiptId());
			ps.setString(3, entity.getCashBalance());
			ps.setTimestamp(4, new java.sql.Timestamp(entity.getCreatedAt().getTime()));
			ps.setLong(5, entity.getCreatedBy());
			ps.setLong(6, entity.getId());
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
			row.setId(rs.getLong("id"));
			row.setMachineId(rs.getLong("machine_id"));
			row.setLastReceiptId(rs.getLong("last_receipt_id"));
			row.setCashBalance(rs.getString("cash_balance"));
			row.setCreatedAt(new Date(rs.getTimestamp("created_at").getTime()));
			row.setCreatedBy(rs.getLong("created_by"));
			return row;
		};
	}
}