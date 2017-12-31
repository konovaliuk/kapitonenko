package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ProductDAO;
import ua.kapitonenko.dao.tables.ProductsTable;
import ua.kapitonenko.domain.Product;

import java.sql.Connection;
import java.util.List;

public class MysqlProductDAO extends BaseDAO<Product> implements ProductDAO {
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
	
	public Product findOne(final Long id) {
		return getRow(getSelectOneNotDeletedQuery(),
				ps -> ps.setLong(1, id),
				getResultSetExtractor());
	}
	
	public List<Product> findAll() {
		return getList(getSelectAllNotDeletedQuery(), getResultSetExtractor());
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(final Product entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setBigDecimal(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setBigDecimal(4, entity.getQuantity());
			ps.setObject(5, entity.getCreatedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final Product entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setBigDecimal(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setBigDecimal(4, entity.getQuantity());
			ps.setLong(5, entity.getId());
		};
	}
	
	
	@Override
	public boolean delete(final Product entity, Long userId) {
		entity.setDeletedBy(userId);
		int result = executeUpdate(DELETE, ps -> {
			ps.setLong(1, entity.getDeletedBy());
			ps.setLong(2, entity.getId());
		});
		return result > 0;
	}
	
	@Override
	protected ResultSetExtractor<Product> getResultSetExtractor() {
		return rs -> {
			Product row = new Product();
			row.setId(rs.getLong(ProductsTable.ID));
			row.setUnitId(rs.getLong(ProductsTable.UNIT));
			row.setPrice(rs.getBigDecimal(ProductsTable.PRICE));
			row.setTaxCategoryId(rs.getLong(ProductsTable.TAX_CAT));
			row.setQuantity(rs.getBigDecimal(ProductsTable.QUANTITY));
			row.setCreatedAt(rs.getTimestamp(ProductsTable.CREATED_AT));
			row.setCreatedBy(rs.getLong(ProductsTable.CREATED_BY));
			row.setDeletedAt(rs.getTimestamp(ProductsTable.DELETED_AT));
			row.setDeletedBy(rs.getLong(ProductsTable.DELETED_BY));
			return row;
		};
	}
}