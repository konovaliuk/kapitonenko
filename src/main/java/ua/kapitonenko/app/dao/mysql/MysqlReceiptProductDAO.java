package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.ReceiptProductDAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.records.ReceiptProduct;
import ua.kapitonenko.app.dao.tables.ReceiptProductsTable;

import java.sql.Connection;
import java.util.List;

public class MysqlReceiptProductDAO extends BaseDAO<ReceiptProduct> implements ReceiptProductDAO {
	private static final String UPDATE = "UPDATE " +
			                                     ReceiptProductsTable.NAME + " SET " +
			                                     ReceiptProductsTable.RECEIPT_ID + " = ?, " +
			                                     ReceiptProductsTable.PRODUCT_ID + " = ?, " +
			                                     ReceiptProductsTable.QUANTITY + " = ? " +
			                                     WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     ReceiptProductsTable.NAME + " (" +
			                                     ReceiptProductsTable.RECEIPT_ID + ", " +
			                                     ReceiptProductsTable.PRODUCT_ID + ", " +
			                                     ReceiptProductsTable.QUANTITY + ") VALUES (?, ?, ? )";
	
	MysqlReceiptProductDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ReceiptProductsTable.NAME;
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
	protected PreparedStatementSetter getInsertStatementSetter(final ReceiptProduct entity) {
		return ps -> {
			ps.setLong(1, entity.getReceiptId());
			ps.setLong(2, entity.getProductId());
			ps.setBigDecimal(3, entity.getQuantity());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ReceiptProduct entity) {
		return ps -> {
			ps.setLong(1, entity.getReceiptId());
			ps.setLong(2, entity.getProductId());
			ps.setBigDecimal(3, entity.getQuantity());
			ps.setLong(4, entity.getId());
		};
	}
	
	
	@Override
	public boolean delete(ReceiptProduct entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected ResultSetExtractor<ReceiptProduct> getResultSetExtractor() {
		return rs -> {
			ReceiptProduct row = new ReceiptProduct();
			row.setId(rs.getLong(ReceiptProductsTable.ID));
			row.setReceiptId(rs.getLong(ReceiptProductsTable.RECEIPT_ID));
			row.setProductId(rs.getLong(ReceiptProductsTable.PRODUCT_ID));
			row.setQuantity(rs.getBigDecimal(ReceiptProductsTable.QUANTITY));
			return row;
		};
	}
	
	
	@Override
	public List<ReceiptProduct> findAllByReceiptId(Long receiptId) {
		return getList(getSelectAllQuery() + " WHERE " + ReceiptProductsTable.RECEIPT_ID + "=?",
				ps -> {
					ps.setLong(1, receiptId);
					
				}, getResultSetExtractor());
	}
}