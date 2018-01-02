package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ProductLocaleDAO;
import ua.kapitonenko.dao.tables.ProductLocaleTable;
import ua.kapitonenko.domain.entities.ProductLocale;

import java.sql.Connection;
import java.util.List;

public class MysqlProductLocaleDAO extends BaseDAO<ProductLocale> implements ProductLocaleDAO {
	private static final String UPDATE = "UPDATE " +
			                                     ProductLocaleTable.NAME + " SET " +
			                                     ProductLocaleTable.PRODUCT_ID + " = ?, " +
			                                     ProductLocaleTable.LOCALE + " = ?, " +
			                                     ProductLocaleTable.PROP_NAME + " = ?, " +
			                                     ProductLocaleTable.PROP_VALUE + " = ? " +
			                                     WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     ProductLocaleTable.NAME + " (" +
			                                     ProductLocaleTable.PRODUCT_ID + ", " +
			                                     ProductLocaleTable.LOCALE + ", " +
			                                     ProductLocaleTable.PROP_NAME + ", " +
			                                     ProductLocaleTable.PROP_VALUE + ") VALUES (?, ?, ?, ?)";
	
	MysqlProductLocaleDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ProductLocaleTable.NAME;
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
	protected PreparedStatementSetter getInsertStatementSetter(ProductLocale entity) {
		return ps -> {
			ps.setLong(1, entity.getProductId());
			ps.setLong(2, entity.getLocaleId());
			ps.setString(3, entity.getPropertyName());
			ps.setString(4, entity.getPropertyValue());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(ProductLocale entity) {
		return ps -> {
			ps.setLong(1, entity.getProductId());
			ps.setLong(2, entity.getLocaleId());
			ps.setString(3, entity.getPropertyName());
			ps.setString(4, entity.getPropertyValue());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	public boolean delete(ProductLocale entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected ResultSetExtractor<ProductLocale> getResultSetExtractor() {
		return rs -> {
			ProductLocale row = new ProductLocale();
			row.setId(rs.getLong(ProductLocaleTable.ID));
			row.setProductId(rs.getLong(ProductLocaleTable.PRODUCT_ID));
			row.setLocaleId(rs.getLong(ProductLocaleTable.LOCALE));
			row.setPropertyName(rs.getString(ProductLocaleTable.PROP_NAME));
			row.setPropertyValue(rs.getString(ProductLocaleTable.PROP_VALUE));
			return row;
		};
	}
	
	@Override
	public List<ProductLocale> findByProductAndKey(Long id, String key) {
		return getList(getSelectAllQuery() + " WHERE " +
				               ProductLocaleTable.PRODUCT_ID + "=? AND " +
				               ProductLocaleTable.PROP_NAME + "=?",
				ps -> {
					ps.setLong(1, id);
					ps.setString(2, key);
				},
				getResultSetExtractor());
	}
	
}