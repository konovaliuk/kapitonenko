package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.ReceiptDAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.tables.ReceiptsTable;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.records.ReceiptRecord;

import java.sql.Connection;
import java.util.List;

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
	
	/*	SELECT * FROM receipts
		WHERE created_at <= (
			SELECT created_at FROM z_reports
				WHERE z_reports.id = 2) AND created_at > IFNULL(
					(SELECT created_at FROM z_reports
				        WHERE cashbox_id=2 AND z_reports.id < 2
				        ORDER BY z_reports.id  DESC LIMIT 1),
				'0000-00-00 00:00:00');
	*/
	@Override
	public List<ReceiptRecord> findAllByZReportId(Long zReportId, Long cashboxId) {
		String query = "WHERE " + ReceiptsTable.CREATED_AT + " <= (" +
				               "  SELECT " + ZReportsTable.CREATED_AT +
				               "  FROM " + ZReportsTable.NAME +
				               "  WHERE " + ZReportsTable.ID + " = ?)" +
				               "  AND " + ReceiptsTable.CREATED_AT + " > IFNULL(" +
				               "  (SELECT " + ZReportsTable.CREATED_AT +
				               "  FROM " + ZReportsTable.NAME +
				               "  WHERE " + ZReportsTable.CASHBOX_ID + "=? " +
				               "  AND " + ZReportsTable.ID + " < ?" +
				               "  ORDER BY " + ZReportsTable.ID + "  DESC LIMIT 1)," +
				               "  '0000-00-00 00:00:00')";
		
		return findAllByQuery(query, ps -> {
			ps.setLong(1, zReportId);
			ps.setLong(2, cashboxId);
			ps.setLong(3, zReportId);
		});
	}
	
	@Override
	public List<ReceiptRecord> findAllByCashboxId(Long cashboxId) {
		String query = "WHERE " + ReceiptsTable.CASHBOX_ID + "=? AND " +
				               ReceiptsTable.CREATED_AT + " > IFNULL((SELECT " +
				               ZReportsTable.CREATED_AT + " FROM " +
				               ZReportsTable.NAME + " WHERE " +
				               ZReportsTable.CASHBOX_ID + "=? ORDER BY " +
				               ZReportsTable.ID + " DESC LIMIT 1), '0000-00-00 00:00:00')";
		
		return findAllByQuery(query, ps -> {
			ps.setLong(1, cashboxId);
			ps.setLong(2, cashboxId);
		});
	}
	
	@Override
	public List<ReceiptRecord> findAll(int offset, int limit) {
		return findAllByQuery("ORDER BY " + ReceiptsTable.ID +
				                      " DESC LIMIT ? OFFSET ?", ps -> {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
		});
	}
}