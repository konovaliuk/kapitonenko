package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.dao.records.ReceiptProduct;
import ua.kapitonenko.app.dao.records.ReceiptRecord;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl extends BaseService implements ReceiptService {
	private static final Logger LOGGER = Logger.getLogger(ReceiptServiceImpl.class);
	
	ReceiptServiceImpl() {
	}
	
	@Override
	public boolean create(Receipt receipt) {
		SettingsService settingsService = getServiceFactory().getSettingsService();
		receipt.setCategories(settingsService.getTaxCatList());
		
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			
			ReceiptRecord record = receipt.getRecord();
			if (receiptDAO.insert(record)) {
				ReceiptRecord created = receiptDAO.findOne(record.getId());
				receipt.setRecord(created);
				return true;
			}
			return false;
		}
	}
	
	@Override
	public boolean createReturn(Receipt receipt) {
		ReceiptRecord existing = receipt.getRecord();
		ReceiptRecord updated = new ReceiptRecord(null,
				                                         existing.getCashboxId(),
				                                         existing.getPaymentTypeId(),
				                                         Application.Ids.RECEIPT_TYPE_RETURN.getValue(),
				                                         true,
				                                         existing.getCreatedBy());
		receipt.setRecord(updated);
		return create(receipt);
		
	}
	
	@Override
	public boolean update(Receipt receipt) {
		boolean updateStock = !receipt.getRecord().isCancelled();
		boolean increase = receipt.getRecord().getReceiptTypeId().equals(Application.Ids.RECEIPT_TYPE_RETURN.getValue());
		return update(receipt, updateStock, increase);
	}
	
	@Override
	public boolean cancel(Long receiptId) {
		Receipt receipt = findOne(receiptId);
		receipt.getRecord().setCancelled(true);
		boolean increase = receipt.getRecord().getReceiptTypeId().equals(Application.Ids.RECEIPT_TYPE_FISCAL.getValue());
		return update(receipt, true, increase);
	}
	
	@Override
	public List<Receipt> getReceiptList(int offset, int limit) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAll(offset, limit);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(Long cashboxId) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAllByCashboxId(cashboxId);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(Long reportId, Long cashboxId) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAllByZReportId(reportId, cashboxId);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public Receipt findOne(Long receiptId) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			ReceiptRecord record = receiptDAO.findOne(receiptId);
			
			Receipt receipt = getModelFactory().createReceipt(record);
			setReferences(receipt, record, connection.open());
			return receipt;
		}
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			return receiptDAO.getCount();
		}
	}
	
	private boolean update(Receipt receipt, boolean updateStock, boolean increase) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			
			connection.beginTransaction();
			
			ReceiptDAO receiptDAO = getDaoFactory().getReceiptDAO(connection.open());
			ReceiptRecord record = receipt.getRecord();
			
			if (receiptDAO.update(record)) {
				ReceiptRecord updated = receiptDAO.findOne(record.getId());
				updateReferences(updated, receipt.getProducts(), updateStock, increase, connection.open());
				receipt.setRecord(updated);
				setReferences(receipt, updated, connection.open());
				connection.commit();
				return true;
			}
			return false;
		}
	}
	
	private void updateReferences(ReceiptRecord record, List<Product> products,
	                              boolean updateStock, boolean increase, Connection connection) {
		LOGGER.debug(products);
		ReceiptProductDAO receiptProductDAO = getDaoFactory().getReceiptProductDAO(connection);
		boolean createRefs = receiptProductDAO
				                     .findAllByReceiptId(record.getId())
				                     .isEmpty();
		
		products.forEach(product -> {
			
			if (createRefs) {
				ReceiptProduct rp = new ReceiptProduct(null, record.getId(), product.getId(), product.getQuantity());
				receiptProductDAO.insert(rp);
			}
			
			if (updateStock) {
				updateQuantityInStock(product, increase, connection);
			}
		});
	}
	
	private void updateQuantityInStock(Product product, boolean increase, Connection connection) {
		ProductDAO productDAO = getDaoFactory().getProductDAO(connection);
		ProductRecord inStockRecord = productDAO.findOne(product.getId());
		Product inStock = getModelFactory().createProduct(inStockRecord);
		
		if (inStock != null) {
			if (increase) {
				inStock.addQuantity(product.getQuantity());
			} else {
				inStock.addQuantity(product.getQuantity().negate());
			}
			productDAO.update(inStock.getRecord());
		}
	}
	
	private void setReferences(Receipt receipt, ReceiptRecord record, Connection connection) {
		ProductService productService = getServiceFactory().getProductService();
		SettingsService settingsService = getServiceFactory().getSettingsService();
		PaymentTypeDAO paymentTypeDAO = getDaoFactory().getPaymentTypeDAO(connection);
		ReceiptTypeDAO receiptTypeDAO = getDaoFactory().getReceiptTypeDAO(connection);
		CashboxDAO cashboxDAO = getDaoFactory().getCashboxDao(connection);
		
		receipt.setProducts(productService.findAllByReceiptId(record.getId()));
		receipt.setCategories(settingsService.getTaxCatList());
		receipt.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
		receipt.setPaymentType(paymentTypeDAO.findOne(record.getPaymentTypeId()));
		receipt.setReceiptType(receiptTypeDAO.findOne(record.getReceiptTypeId()));
		
	}
	
	private void fillList(List<Receipt> receipts, List<ReceiptRecord> records, Connection connection) {
		records.forEach(record -> {
			Receipt receipt = getModelFactory().createReceipt(record);
			setReferences(receipt, record, connection);
			receipts.add(receipt);
		});
	}
}
