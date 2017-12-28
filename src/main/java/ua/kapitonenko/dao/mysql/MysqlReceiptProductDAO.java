package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ReceiptProductDAO;
import ua.kapitonenko.dao.tables.ReceiptProductsTable;
import ua.kapitonenko.domain.ReceiptProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlReceiptProductDAO extends BaseDAO<ReceiptProduct> implements ReceiptProductDAO {
	private static final String UPDATE = "" + WHERE_ID;
	private static final String INSERT = "";
	
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
		return new PreparedStatementSetter() {
			public void prepare(PreparedStatement ps) throws SQLException {
				ps.setLong(1, entity.getReceiptId());
				ps.setLong(2, entity.getProductId());
				ps.setString(3, entity.getQuantity());
			}
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ReceiptProduct entity) {
		return new PreparedStatementSetter() {
			public void prepare(PreparedStatement ps) throws SQLException {
				ps.setLong(1, entity.getReceiptId());
				ps.setLong(2, entity.getProductId());
				ps.setString(3, entity.getQuantity());
				ps.setLong(4, entity.getId());
			}
		};
	}
	
	
	@Override
	public boolean delete(ReceiptProduct entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected ResultSetExtractor<ReceiptProduct> getResultSetExtractor() {
		return new ResultSetExtractor<ReceiptProduct>() {
			public ReceiptProduct extract(ResultSet rs) throws SQLException {
				ReceiptProduct row = new ReceiptProduct();
				row.setId(rs.getLong("id"));
				row.setReceiptId(rs.getLong("receipt_id"));
				row.setProductId(rs.getLong("product_id"));
				row.setQuantity(rs.getString("quantity"));
				return row;
			}
		};
	}
}