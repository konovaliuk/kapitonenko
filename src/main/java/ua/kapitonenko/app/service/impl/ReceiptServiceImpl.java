package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionPool;
import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.tables.ReceiptsTable;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ReceiptProduct;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.domain.records.ReceiptType;
import ua.kapitonenko.app.exceptions.DAOException;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger LOGGER = Logger.getLogger(ReceiptServiceImpl.class);
	
	private static ReceiptServiceImpl instance = new ReceiptServiceImpl();
	
	private ConnectionPool pool = Application.getConnectionPool();
	
	private ReceiptServiceImpl() {
	}
	
	public static ReceiptServiceImpl getInstance() {
		return instance;
	}
	
/*	private void setReferences(Receipt record, Connection connection) {
		SettingsService settingsService = Application.getServiceFactory().getSettingsService();
		UserService userService = Application.getServiceFactory().getUserService();
		
		record.setCashbox(settingsService.findCashbox(record.getCashboxId()));
		record.setPaymentType(settingsService.findPaymentType(record.getPaymentTypeId()));
		record.setReceiptType(findReceiptType(record.getReceiptTypeId()));
		record.setUserCreateBy((userService.findById(record.getCreatedBy())));
		record.setProducts(findReceiptProducts(record.getId()));
	}*/
	
	// TODO chose implementation
	private void setReferences(ReceiptRecord record, Connection connection) {
		CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection);
		PaymentTypeDAO paymentTypeDAO = Application.getDAOFactory().getPaymentTypeDAO(connection);
		ReceiptTypeDAO receiptTypeDAO = Application.getDAOFactory().getReceiptTypeDAO(connection);
		UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
		ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
		
		record.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
		record.setPaymentType(paymentTypeDAO.findOne(record.getPaymentTypeId()));
		record.setReceiptType(receiptTypeDAO.findOne(record.getReceiptTypeId()));
		record.setUserCreateBy(userDAO.findOne(record.getCreatedBy()));
		record.setProducts(receiptProductDAO.findAllByReceiptId(record.getId()));
	}
	
	
	@Override
	public boolean update(Receipt calculator) {
		
		Connection connection = null;
		try {
			connection = pool.getConnection();
			connection.setAutoCommit(false);
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			
			ReceiptRecord receipt = calculator.getRecord();
			
			if (receiptDAO.update(receipt)) {
				
				ReceiptRecord created = receiptDAO.findOne(receipt.getId());
				
				calculator.getProducts().forEach(product -> {
					ReceiptProduct rp = new ReceiptProduct(null, created.getId(), product.getId(), product.getQuantity());
					receiptProductDAO.insert(rp);
					Product inStock = productDAO.findOne(product.getId());
					inStock.addQuantity(product.getQuantity().negate());
					productDAO.update(inStock);
				});
				
				setReferences(created, connection);
				calculator.setRecord(created);
				connection.commit();
				return true;
			}
			
			return false;
		} catch (SQLException e) {
			try {
				connection.rollback();
				return false;
			} catch (SQLException e1) {
				throw new DAOException(e);
			}
			
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public boolean create(Receipt calculator) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			
			ReceiptRecord receipt = calculator.getRecord();
			if (receiptDAO.insert(receipt)) {
				ReceiptRecord created = receiptDAO.findOne(receipt.getId());
				setReferences(created, connection);
				calculator.setRecord(created);
				return true;
			}
			return false;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(int offset, int limit) {
		return getReceiptList("ORDER BY ? LIMIT ? OFFSET ?", ps -> {
			ps.setString(1, ReceiptsTable.ID);
			ps.setInt(3, offset);
			ps.setInt(2, limit);
		});
		
	}
	
	@Override
	public List<Receipt> getReceiptList(Long cashboxId) {
		
		String query = "WHERE " + ReceiptsTable.CASHBOX_ID + "=? AND " +
				               ReceiptsTable.CREATED_AT + " > IFNULL((SELECT " +
				               ZReportsTable.CREATED_AT + " FROM " +
				               ZReportsTable.NAME + " WHERE " +
				               ZReportsTable.CASHBOX_ID + "=? ORDER BY " +
				               ZReportsTable.ID + " DESC LIMIT 1), '0000-00-00 00:00:00')";
		
		return getReceiptList(query, ps -> {
			ps.setLong(1, cashboxId);
			ps.setLong(2, cashboxId);
		});
	}
	
	@Override
	public ReceiptRecord findById(Long receiptId) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ReceiptRecord receipt = receiptDAO.findOne(receiptId);
			setReferences(receipt, connection);
			return receipt;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public boolean cancel(Long receiptId) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ReceiptRecord receipt = receiptDAO.findOne(receiptId);
			if (receipt != null) {
				receipt.setCancelled(true);
				return receiptDAO.update(receipt);
			}
			
			return false;
			
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public long getReceiptsCount() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			return receiptDAO.getCount();
		} finally {
			pool.close(connection);
		}
	}
	
/*	@Override
	public List<Receipt> getSales(Long cashboxId) {
		String temp = "SELECT * FROM receipts\n" +
				              "WHERE cashbox_id=2\n" +
				              "      AND created_at > IFNULL(\n" +
				              "    (SELECT created_at FROM z_reports WHERE cashbox_id=2 ORDER BY id DESC LIMIT 1),\n" +
				              "    '0000-00-00 00:00:00');"
		String query = "WHERE " + ReceiptsTable.CASHBOX_ID + "=? AND " + ReceiptsTable.RECEIPT_TYPE_ID + "=?";
		return getReceiptList(query, ps -> {
			ps.setLong(1, cashboxId);
			ps.setLong(2, Application.getId(Application.RECEIPT_TYPE_FISCAL));
		});
	}
	
	@Override
	public List<Receipt> getRefunds(Long cashboxId) {
		String query = "WHERE " + ReceiptsTable.CASHBOX_ID + "=? AND " + ReceiptsTable.RECEIPT_TYPE_ID + "=?";
		return getReceiptList(query, ps -> {
			ps.setLong(1, cashboxId);
			ps.setLong(2, Application.getId(Application.RECEIPT_TYPE_RETURN));
		});
	}*/
	
	
	private List<Receipt> getReceiptList(String sql, PreparedStatementSetter ps) {
		List<Receipt> receiptList = new ArrayList<>();
		Connection connection = pool.getConnection();
		try {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ProductService productService = Application.getServiceFactory().getProductService();
			SettingsService settingsService = Application.getServiceFactory().getSettingsService();
			List<ReceiptRecord> list = receiptDAO.findAllByQuery(sql, ps);
			
			list.forEach(record -> {
				setReferences(record, connection);
				Receipt receipt = new Receipt(record);
				receipt.setProducts(productService.findAllByReceiptId(record.getId()));
				receipt.setCategories(settingsService.getTaxCatList());
				receiptList.add(receipt);
			});
			LOGGER.debug(receiptList);
			return receiptList;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public ReceiptType findReceiptType(Long id) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptTypeDAO receiptTypeDAO = Application.getDAOFactory().getReceiptTypeDAO(connection);
			return receiptTypeDAO.findOne(id);
		} finally {
			pool.close(connection);
		}
	}
	
	
	public List<ReceiptProduct> findReceiptProducts(Long receiptId) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
			return receiptProductDAO.findAllByReceiptId(receiptId);
		} finally {
			pool.close(connection);
		}
	}
	
	
}
