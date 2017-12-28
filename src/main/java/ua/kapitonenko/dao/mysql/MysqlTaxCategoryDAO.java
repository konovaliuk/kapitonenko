package ua.kapitonenko.dao.mysql;

import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.TaxCategoryDAO;
import ua.kapitonenko.dao.tables.BaseTable;
import ua.kapitonenko.dao.tables.TaxCategoriesTable;
import ua.kapitonenko.domain.TaxCategory;

import java.sql.Connection;

public class MysqlTaxCategoryDAO extends BaseLocalizedDAO<TaxCategory> implements TaxCategoryDAO {
	
	MysqlTaxCategoryDAO(Connection connection) {
		super(connection);
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
			return row;
		};
	}
	
	
}