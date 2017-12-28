package ua.kapitonenko.dao.mysql;


import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.dao.helpers.ResultSetExtractor;
import ua.kapitonenko.dao.interfaces.ZReportDetailDAO;
import ua.kapitonenko.dao.tables.ZReportDetailsTable;
import ua.kapitonenko.domain.ZReportDetail;

import java.sql.Connection;

public class MysqlZReportDetailDAO extends BaseDAO<ZReportDetail> implements ZReportDetailDAO {
	
	private static final String UPDATE = "" + WHERE_ID;
	private static final String INSERT = "";
	
	MysqlZReportDetailDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return ZReportDetailsTable.NAME;
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
	protected PreparedStatementSetter getInsertStatementSetter(final ZReportDetail entity) {
		return ps -> {
			ps.setLong(1, entity.getZReportId());
			ps.setLong(2, entity.getReceiptTypeId());
			ps.setLong(3, entity.getReceiptsNumber());
			ps.setLong(4, entity.getCancelledNumber());
			ps.setLong(5, entity.getProductsNumber());
			ps.setString(6, entity.getCashAmount());
			ps.setString(7, entity.getCat1Amount());
			ps.setString(8, entity.getCat2Amount());
			ps.setString(9, entity.getTotalAmount());
			ps.setString(10, entity.getTax1Amount());
			ps.setString(11, entity.getTax2Amount());
			ps.setString(12, entity.getTaxTaxAmount());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(final ZReportDetail entity) {
		return ps -> {
			ps.setLong(1, entity.getZReportId());
			ps.setLong(2, entity.getReceiptTypeId());
			ps.setLong(3, entity.getReceiptsNumber());
			ps.setLong(4, entity.getCancelledNumber());
			ps.setLong(5, entity.getProductsNumber());
			ps.setString(6, entity.getCashAmount());
			ps.setString(7, entity.getCat1Amount());
			ps.setString(8, entity.getCat2Amount());
			ps.setString(9, entity.getTotalAmount());
			ps.setString(10, entity.getTax1Amount());
			ps.setString(11, entity.getTax2Amount());
			ps.setString(12, entity.getTaxTaxAmount());
			ps.setLong(13, entity.getId());
		};
	}
	
	@Override
	public boolean delete(ZReportDetail entity, Long userId) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected ResultSetExtractor<ZReportDetail> getResultSetExtractor() {
		return rs -> {
			ZReportDetail row = new ZReportDetail();
			row.setId(rs.getLong("id"));
			row.setZReportId(rs.getLong("z_report_id"));
			row.setReceiptTypeId(rs.getLong("receipt_type_id"));
			row.setReceiptsNumber(rs.getLong("receipts_number"));
			row.setCancelledNumber(rs.getLong("cancelled_number"));
			row.setProductsNumber(rs.getLong("products_number"));
			row.setCashAmount(rs.getString("cash_amount"));
			row.setCat1Amount(rs.getString("cat_1_amount"));
			row.setCat2Amount(rs.getString("cat_2_amount"));
			row.setTotalAmount(rs.getString("total_amount"));
			row.setTax1Amount(rs.getString("tax_1_amount"));
			row.setTax2Amount(rs.getString("tax_2_amount"));
			row.setTaxTaxAmount(rs.getString("tax_tax_amount"));
			return row;
		};
	}
}