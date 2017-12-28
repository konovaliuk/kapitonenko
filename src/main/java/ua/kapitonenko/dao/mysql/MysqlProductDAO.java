package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ProductDAO;
import ua.kapitonenko.dao.tables.ProductsTable;
import ua.kapitonenko.domain.Product;

import java.sql.Connection;

public class MysqlProductDAO extends BaseDAO<Product> implements ProductDAO {
	private static final String UPDATE = "UPDATE products SET unit_id = ?, price = ?, tax_category_id = ?, quantity = ?, created_at = ?, created_by = ?, deleted_at = ?, deleted_by = ?" + WHERE_ID;
	private static final String DELETE = "UPDATE products SET deleted_at = NOW(), deleted_by = ?" + WHERE_ID;
	private static final String INSERT = "INSERT INTO products ( unit_id, price, tax_category_id, quantity, created_at, created_by, deleted_at, deleted_by) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";
	
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
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(final Product entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setString(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setString(4, entity.getQuantity());
			ps.setTimestamp(5, new java.sql.Timestamp(entity.getCreatedAt().getTime()));
			ps.setLong(6, entity.getCreatedBy());
			ps.setTimestamp(7, new java.sql.Timestamp(entity.getDeletedAt().getTime()));
			ps.setLong(8, entity.getDeletedBy());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final Product entity) {
		return ps -> {
			ps.setLong(1, entity.getUnitId());
			ps.setString(2, entity.getPrice());
			ps.setLong(3, entity.getTaxCategoryId());
			ps.setString(4, entity.getQuantity());
			ps.setTimestamp(5, entity.getCreatedAt());
			ps.setLong(6, entity.getCreatedBy());
			ps.setTimestamp(7, entity.getDeletedAt());
			ps.setLong(8, entity.getDeletedBy());
			ps.setLong(9, entity.getId());
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
			row.setId(rs.getLong("id"));
			row.setUnitId(rs.getLong("unit_id"));
			row.setPrice(rs.getString("price"));
			row.setTaxCategoryId(rs.getLong("tax_category_id"));
			row.setQuantity(rs.getString("quantity"));
			row.setCreatedAt(rs.getTimestamp("created_at"));
			row.setCreatedBy(rs.getLong("created_by"));
			row.setDeletedAt(rs.getTimestamp("deleted_at"));
			row.setDeletedBy(rs.getLong("deleted_by"));
			return row;
		};
	}
}