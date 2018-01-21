package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.interfaces.ProductDAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.dao.tables.ProductLocaleTable;
import ua.kapitonenko.app.dao.tables.ProductsTable;
import ua.kapitonenko.app.dao.tables.ReceiptProductsTable;

import java.sql.Connection;
import java.util.List;

public class MysqlProductDAO extends BaseDAO<ProductRecord> implements ProductDAO {
	
	private static final String UPDATE = "UPDATE " +
			                                     ProductsTable.NAME + " SET " +
			                                     ProductsTable.UNIT + " = ?, " +
			                                     ProductsTable.PRICE + " = ?, " +
			                                     ProductsTable.TAX_CAT + " = ?, " +
			                                     ProductsTable.QUANTITY + " = ? " +
			                                     WHERE_ID;
	private static final String DELETE = "UPDATE " +
			                                     ProductsTable.NAME + " SET " +
			                                     ProductsTable.DELETED_AT + " = NOW(), " +
			                                     ProductsTable.DELETED_BY + " = ? " +
			                                     WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     ProductsTable.NAME + " ( " +
			                                     ProductsTable.UNIT + ", " +
			                                     ProductsTable.PRICE + ", " +
			                                     ProductsTable.TAX_CAT + ", " +
			                                     ProductsTable.QUANTITY + ", " +
			                                     ProductsTable.CREATED_AT + ", " +
			                                     ProductsTable.CREATED_BY +
			                                     ") VALUES (?, ?, ?, ?, NOW(), ?)";
	
	private static final String SELECT_BY_ID_OR_NAME = "SELECT * FROM " +
			                                                   ProductsTable.NAME + " JOIN " +
			                                                   ProductLocaleTable.NAME + " ON " +
			                                                   ProductsTable.ID + " = " +
			                                                   ProductLocaleTable.PRODUCT_ID + " WHERE (( " +
			                                                   ProductLocaleTable.PROP_NAME + " = ? AND " +
			                                                   ProductLocaleTable.LOCALE + " = ? AND " +
			                                                   ProductLocaleTable.PROP_VALUE + " = ? ) OR ( " +
			                                                   ProductsTable.ID + " = ?))" +
			                                                   AND_NOT_DELETED + " GROUP BY " +
			                                                   ProductsTable.ID;
	
	private static final String SELECT_BY_RECEIPT_ID = "SELECT *, " + ReceiptProductsTable.QUANTITY +
			                                                   " AS " + ProductsTable.QUANTITY_ALIAS +
			                                                   " FROM " + ReceiptProductsTable.NAME +
			                                                   " JOIN " + ProductsTable.NAME + " ON " +
			                                                   ProductsTable.ID + " = " + ReceiptProductsTable.PRODUCT_ID +
			                                                   " WHERE " + ReceiptProductsTable.RECEIPT_ID + " = ?";
	
	
	MysqlProductDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ProductsTable.NAME;
	}
	
	@Override
	protected String getInsertQuery() {
		return INSERT;
	}
	
	@Override
	protected String getUpdateQuery() {
		return UPDATE;
	}
	
	protected String getCountQuery() {
		return super.getCountQuery() + WHERE_NOT_DELETED;
	}
	
	public ProductRecord findOne(final Long id) {
		return getRow(getSelectOneNotDeletedQuery(),
				ps -> ps.setLong(1, id),
				getResultSetExtractor());
	}
	
	public List<ProductRecord> findAll() {
		return getList(getSelectAllNotDeletedQuery(), getResultSetExtractor());
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(final ProductRecord entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setBigDecimal(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setBigDecimal(4, entity.getQuantity());
			ps.setObject(5, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ProductRecord entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setBigDecimal(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setBigDecimal(4, entity.getQuantity());
			ps.setLong(5, entity.getId());
		};
	}
	
	
	@Override
	public boolean delete(final ProductRecord entity, Long userId) {
		entity.setDeletedBy(userId);
		int result = executeUpdate(DELETE, ps -> {
			ps.setLong(1, entity.getDeletedBy());
			ps.setLong(2, entity.getId());
		});
		return result > 0;
	}
	
	@Override
	protected ResultSetExtractor<ProductRecord> getResultSetExtractor() {
		return rs -> {
			ProductRecord row = new ProductRecord();
			row.setId(rs.getLong(ProductsTable.ID));
			row.setUnitId(rs.getLong(ProductsTable.UNIT));
			row.setPrice(rs.getBigDecimal(ProductsTable.PRICE));
			row.setTaxCategoryId(rs.getLong(ProductsTable.TAX_CAT));
			row.setQuantity(rs.getBigDecimal(ProductsTable.QUANTITY_ALIAS));
			row.setCreatedAt(rs.getTimestamp(ProductsTable.CREATED_AT));
			row.setCreatedBy(rs.getLong(ProductsTable.CREATED_BY));
			row.setDeletedAt(rs.getTimestamp(ProductsTable.DELETED_AT));
			row.setDeletedBy(rs.getLong(ProductsTable.DELETED_BY));
			return row;
		};
	}
	
	@Override
	public List<ProductRecord> findByIdOrName(Long localeId, Long productId, String name) {
		return getList(SELECT_BY_ID_OR_NAME, ps -> {
			ps.setString(1, Keys.PRODUCT_NAME);
			ps.setLong(2, localeId);
			ps.setString(3, name);
			ps.setObject(4, productId);
			
		}, getResultSetExtractor());
	}
	
	@Override
	public List<ProductRecord> findAllByReceiptId(Long receiptId) {
		return getList(SELECT_BY_RECEIPT_ID, ps -> {
			ps.setLong(1, receiptId);
			
		}, getResultSetExtractor());
	}
	
	@Override
	public List<ProductRecord> findAllByQuery(String query, PreparedStatementSetter pss) {
		return getList(getSelectAllNotDeletedQuery() + " " + query, pss, getResultSetExtractor());
	}
	
}