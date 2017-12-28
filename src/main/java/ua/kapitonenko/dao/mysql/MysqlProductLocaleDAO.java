package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ProductLocaleDAO;
import ua.kapitonenko.dao.tables.ProductLocaleTable;
import ua.kapitonenko.domain.ProductLocale;

import java.sql.Connection;

public class MysqlProductLocaleDAO extends BaseDAO<ProductLocale> implements ProductLocaleDAO {
	private static final String UPDATE = "" + WHERE_ID;
	private static final String INSERT = "";
	
	
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
	protected PreparedStatementSetter getInsertStatementSetter(final ProductLocale entity) {
		return ps -> {
			ps.setLong(1, entity.getProductId());
			ps.setLong(2, entity.getLocale());
			ps.setString(3, entity.getPropertyName());
			ps.setString(4, entity.getPropertyValue());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ProductLocale entity) {
		return ps -> {
			ps.setLong(1, entity.getProductId());
			ps.setLong(2, entity.getLocale());
			ps.setString(3, entity.getPropertyName());
			ps.setString(4, entity.getPropertyValue());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	public boolean delete(ProductLocale entity, Long userId) {
		return false;
	}
	
	@Override
	protected ResultSetExtractor<ProductLocale> getResultSetExtractor() {
		return rs -> {
			ProductLocale row = new ProductLocale();
			row.setId(rs.getLong("id"));
			row.setProductId(rs.getLong("product_id"));
			row.setLocale(rs.getLong("locale"));
			row.setPropertyName(rs.getString("property_name"));
			row.setPropertyValue(rs.getString("property_value"));
			return row;
		};
	}
	
}