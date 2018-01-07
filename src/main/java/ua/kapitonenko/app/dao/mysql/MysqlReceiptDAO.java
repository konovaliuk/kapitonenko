package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.ReceiptDAO;
import ua.kapitonenko.app.dao.tables.ReceiptsTable;
import ua.kapitonenko.app.domain.records.ReceiptRecord;

import java.sql.Connection;

public class MysqlReceiptDAO extends BaseDAO<ReceiptRecord> implements ReceiptDAO {
	private static final String UPDATE = "UPDATE " +
			                                     ReceiptsTable.NAME + " SET " +
			                                     ReceiptsTable.CASHBOX_ID + " = ?, " +
			                                     ReceiptsTable.PAYMENT_TYPE_ID + " = ?, " +
			                                     ReceiptsTable.RECEIPT_TYPE_ID + " = ?, " +
			                                     ReceiptsTable.CANCELLED + " = ? " +
			                                     WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     ReceiptsTable.NAME + " ( " +
			                                     ReceiptsTable.CASHBOX_ID + ", " +
			                                     ReceiptsTable.PAYMENT_TYPE_ID + ", " +
			                                     ReceiptsTable.RECEIPT_TYPE_ID + ", " +
			                                     ReceiptsTable.CANCELLED + ", " +
			                                     ReceiptsTable.CREATED_AT + ", " +
			                                     ReceiptsTable.CREATED_BY +
			                                     ") VALUES (?, ?, ?, ?, NOW(), ?)";
	
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
	protected PreparedStatementSetter getInsertStatementSetter(final ReceiptRecord entity) {
		return ps -> {
			ps.setLong(1, entity.getCashboxId());
			ps.setLong(2, entity.getPaymentTypeId());
			ps.setLong(3, entity.getReceiptTypeId());
			ps.setBoolean(4, entity.isCancelled());
			ps.setLong(5, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ReceiptRecord entity) {
		return ps -> {
			ps.setLong(1, entity.getCashboxId());
			ps.setLong(2, entity.getPaymentTypeId());
			ps.setLong(3, entity.getReceiptTypeId());
			ps.setBoolean(4, entity.isCancelled());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	public boolean delete(ReceiptRecord entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected ResultSetExtractor<ReceiptRecord> getResultSetExtractor() {
		return rs -> {
			ReceiptRecord row = new ReceiptRecord();
			row.setId(rs.getLong(ReceiptsTable.ID));
			row.setCashboxId(rs.getLong(ReceiptsTable.CASHBOX_ID));
			row.setPaymentTypeId(rs.getLong(ReceiptsTable.PAYMENT_TYPE_ID));
			row.setReceiptTypeId(rs.getLong(ReceiptsTable.RECEIPT_TYPE_ID));
			row.setCancelled(rs.getBoolean(ReceiptsTable.CANCELLED));
			row.setCreatedAt(rs.getTimestamp(ReceiptsTable.CREATED_AT));
			row.setCreatedBy(rs.getLong(ReceiptsTable.CREATED_BY));
			return row;
		};
	}
}