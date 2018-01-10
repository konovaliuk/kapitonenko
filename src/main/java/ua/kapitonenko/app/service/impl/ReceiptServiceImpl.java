package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.tables.ReceiptsTable;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ReceiptProduct;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.domain.records.ReceiptType;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger LOGGER = Logger.getLogger(ReceiptServiceImpl.class);
	
	@Override
	public boolean update(Receipt receipt) {
		boolean updateStock = !receipt.getRecord().isCancelled();
		boolean increase = receipt.getRecord().getReceiptTypeId().equals(Application.getId(Application.RECEIPT_TYPE_RETURN));
		return update(receipt, updateStock, increase);
	}
	
	@Override
	public boolean cancel(Long receiptId) {
		Receipt receipt = findOne(receiptId);
		receipt.getRecord().setCancelled(true);
		boolean increase = receipt.getRecord().getReceiptTypeId().equals(Application.getId(Application.RECEIPT_TYPE_FISCAL));
		return update(receipt, true, increase);
	}
	
	private boolean update(Receipt receipt, boolean updateStock, boolean increase) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			
			connection.beginTransaction();
			
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection.open());
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection.open());
			
			ReceiptRecord record = receipt.getRecord();
			
			if (receiptDAO.update(record)) {
				
				ReceiptRecord updated = receiptDAO.findOne(record.getId());
				
				boolean createRefs = receiptProductDAO.findAllByReceiptId(updated.getId()).isEmpty();
				
				receipt.getProducts().forEach(product -> {
					
					if (createRefs) {
						ReceiptProduct rp = new ReceiptProduct(null, updated.getId(), product.getId(), product.getQuantity());
						receiptProductDAO.insert(rp);
					}
					
					if (updateStock) {
						Product inStock = productDAO.findOne(product.getId());
						
						if (increase) {
							inStock.addQuantity(product.getQuantity());
						} else {
							inStock.addQuantity(product.getQuantity().negate());
						}
						
						productDAO.update(inStock);
					}
				});
				
				setReferences(updated, connection.open());
				receipt.setRecord(updated);
				connection.commit();
				return true;
			}
			return false;
		}
	}
	
	@Override
	public boolean create(Receipt calculator) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			
			ReceiptRecord receipt = calculator.getRecord();
			if (receiptDAO.insert(receipt)) {
				ReceiptRecord created = receiptDAO.findOne(receipt.getId());
				setReferences(created, connection.open());
				calculator.setRecord(created);
				return true;
			}
			return false;
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(int offset, int limit) {
		return getReceiptList("ORDER BY " + ReceiptsTable.ID +
				                      " DESC LIMIT ? OFFSET ?", ps -> {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
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
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			ReceiptRecord receipt = receiptDAO.findOne(receiptId);
			setReferences(receipt, connection.open());
			return receipt;
		}
	}
	
	@Override
	public Receipt findOne(Long receiptId) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			ProductService productService = Application.getServiceFactory().getProductService();
			SettingsService settingsService = Application.getServiceFactory().getSettingsService();
			ReceiptRecord record = receiptDAO.findOne(receiptId);
			
			setReferences(record, connection.open());
			Receipt receipt = new Receipt(record);
			
			LOGGER.debug(record.getId());
			
			receipt.setProducts(productService.findAllByReceiptId(record.getId()));
			receipt.setCategories(settingsService.getTaxCatList());
			
			return receipt;
		}
	}
	
	@Override
	public long getReceiptsCount() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			return receiptDAO.getCount();
		}
	}
	
	private List<Receipt> getReceiptList(String sql, PreparedStatementSetter ps) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection.open());
			ProductService productService = Application.getServiceFactory().getProductService();
			SettingsService settingsService = Application.getServiceFactory().getSettingsService();
			List<ReceiptRecord> list = receiptDAO.findAllByQuery(sql, ps);
			
			list.forEach(record -> {
				setReferences(record, connection.open());
				Receipt receipt = new Receipt(record);
				receipt.setProducts(productService.findAllByReceiptId(record.getId()));
				receipt.setCategories(settingsService.getTaxCatList());
				receiptList.add(receipt);
			});
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public ReceiptType findReceiptType(Long id) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptTypeDAO receiptTypeDAO = Application.getDAOFactory().getReceiptTypeDAO(connection.open());
			return receiptTypeDAO.findOne(id);
		}
	}
	
	public List<ReceiptProduct> findReceiptProducts(Long receiptId) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection.open());
			return receiptProductDAO.findAllByReceiptId(receiptId);
		}
	}
	
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
}
