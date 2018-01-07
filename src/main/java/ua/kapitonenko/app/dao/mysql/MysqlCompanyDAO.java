package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.interfaces.CompanyDAO;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.CompaniesTable;
import ua.kapitonenko.app.domain.records.Company;

import java.sql.Connection;

public class MysqlCompanyDAO extends BaseDAO<Company> implements CompanyDAO {
	
	private static final String UPDATE = "UPDATE " + CompaniesTable.NAME + " SET " +
			                                     CompaniesTable.PN_NUMBER + " = ?, " +
			                                     CompaniesTable.BUNDLE_NAME + " = ?, " +
			                                     CompaniesTable.BUNDLE_KEY_NAME + " = ?, " +
			                                     CompaniesTable.BUNDLE_KEY_ADDRESS + " = ?" + WHERE_ID;
	
	private static final String INSERT = "INSERT INTO " +
			                                     CompaniesTable.NAME + " ( " +
			                                     CompaniesTable.PN_NUMBER + ", " +
			                                     CompaniesTable.BUNDLE_NAME + ", " +
			                                     CompaniesTable.BUNDLE_KEY_NAME + ", " +
			                                     CompaniesTable.BUNDLE_KEY_ADDRESS + ") VALUES (?, ?, ?, ? )";
	
	MysqlCompanyDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return CompaniesTable.NAME;
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
	protected PreparedStatementSetter getInsertStatementSetter(Company entity) {
		return ps -> {
			ps.setString(1, entity.getPnNumber());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKeyName());
			ps.setString(4, entity.getBundleKeyAddress());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(Company entity) {
		return ps -> {
			ps.setString(1, entity.getPnNumber());
			ps.setString(2, entity.getBundleName());
			ps.setString(3, entity.getBundleKeyName());
			ps.setString(4, entity.getBundleKeyAddress());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	public boolean delete(Company entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected ResultSetExtractor<Company> getResultSetExtractor() {
		return rs -> {
			Company row = new Company();
			row.setId(rs.getLong(BaseTable.ID));
			row.setPnNumber(rs.getString(CompaniesTable.PN_NUMBER));
			row.setBundleName(rs.getString(CompaniesTable.BUNDLE_NAME));
			row.setBundleKeyName(rs.getString(CompaniesTable.BUNDLE_KEY_NAME));
			row.setBundleKeyAddress(rs.getString(CompaniesTable.BUNDLE_KEY_ADDRESS));
			return row;
		};
	}
}