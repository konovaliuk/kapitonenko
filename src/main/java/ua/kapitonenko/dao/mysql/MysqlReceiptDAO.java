package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ReceiptDAO;
import ua.kapitonenko.dao.tables.ReceiptsTable;
import ua.kapitonenko.domain.Receipt;

import java.sql.Connection;
import java.util.Date;

public class MysqlReceiptDAO extends BaseDAO<Receipt> implements ReceiptDAO {
	private static final String UPDATE = "" + WHERE_ID;
	private static final String INSERT = "";
	
	MysqlReceiptDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ReceiptsTable.NAME;
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
	protected PreparedStatementSetter getInsertStatementSetter(final Receipt entity) {
		return ps -> {
			ps.setLong(1, entity.getMachineId());
			ps.setLong(2, entity.getPaymentTypeId());
			ps.setLong(3, entity.getReceiptTypeId());
			ps.setLong(4, entity.getCancelled());
			ps.setTimestamp(5, new java.sql.Timestamp(entity.getCreatedAt().getTime()));
			ps.setLong(6, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final Receipt entity) {
		return ps -> {
			ps.setLong(1, entity.getMachineId());
			ps.setLong(2, entity.getPaymentTypeId());
			ps.setLong(3, entity.getReceiptTypeId());
			ps.setLong(4, entity.getCancelled());
			ps.setTimestamp(5, new java.sql.Timestamp(entity.getCreatedAt().getTime()));
			ps.setLong(6, entity.getCreatedBy());
			ps.setLong(7, entity.getId());
		};
	}
	
	@Override
	public boolean delete(Receipt entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected ResultSetExtractor<Receipt> getResultSetExtractor() {
		return rs -> {
			Receipt row = new Receipt();
			row.setId(rs.getLong("id"));
			row.setMachineId(rs.getLong("machine_id"));
			row.setPaymentTypeId(rs.getLong("payment_type_id"));
			row.setReceiptTypeId(rs.getLong("receipt_type_id"));
			row.setCancelled(rs.getLong("cancelled"));
			row.setCreatedAt(new Date(rs.getTimestamp("created_at").getTime()));
			row.setCreatedBy(rs.getLong("created_by"));
			return row;
		};
	}
	
	
}