package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.TaxCategoryDAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.TaxCategoriesTable;

import java.sql.Connection;

public class MysqlTaxCategoryDAO extends BaseLocalizedDAO<TaxCategory> implements TaxCategoryDAO {
	
	MysqlTaxCategoryDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getInsertQuery() {
		return "INSERT INTO " + getTableName() + " (" +
				       BaseTable.COLUMN_NAME + ", " +
				       BaseTable.BUNDLE_NAME + ", " +
				       BaseTable.BUNDLE_KEY + ", " +
					   TaxCategoriesTable.RATE + ") " +
				       "VALUES (?, ?, ?, ? )";
	}
	
	@Override
	protected String getUpdateQuery() {
		return "UPDATE " + getTableName() + " SET " +
				       BaseTable.COLUMN_NAME + " = ?, " +
				       BaseTable.BUNDLE_NAME + " = ?, " +
				       BaseTable.BUNDLE_KEY + " = ?," +
				       TaxCategoriesTable.RATE + " = ?" +
				       WHERE_ID;
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(TaxCategory entity) {
		return ps -> {
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKey());
			ps.setBigDecimal(4, entity.getRate());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(TaxCategory entity) {
		return ps -> {
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKey());
			ps.setBigDecimal(4, entity.getRate());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	protected String getTableName() {
		return TaxCategoriesTable.NAME;
	}
	
	@Override
	protected ResultSetExtractor<TaxCategory> getResultSetExtractor() {
		return rs -> {
			TaxCategory row = new TaxCategory();
			row.setId(rs.getLong(BaseTable.ID));
			row.setName(rs.getString(BaseTable.COLUMN_NAME));
			row.setBundleName(rs.getString(BaseTable.BUNDLE_NAME));
			row.setBundleKey(rs.getString(BaseTable.BUNDLE_KEY));
			row.setRate(rs.getBigDecimal(TaxCategoriesTable.RATE));
			return row;
		};
	}
	
	
}