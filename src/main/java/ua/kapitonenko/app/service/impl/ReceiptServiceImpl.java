package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ReceiptProduct;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.SettingsService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger LOGGER = Logger.getLogger(ReceiptServiceImpl.class);
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	private ServiceFactory serviceFactory;
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	private ServiceFactory getServiceFactory() {
		if (serviceFactory == null) {
			serviceFactory = Application.getServiceFactory();
		}
		return serviceFactory;
	}
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public boolean create(Receipt receipt) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			
			ReceiptRecord record = receipt.getRecord();
			if (receiptDAO.insert(record)) {
				ReceiptRecord created = receiptDAO.findOne(record.getId());
				setReferences(created, connection.open());
				receipt.setRecord(created);
				return true;
			}
			return false;
		}
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
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAll(offset, limit);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(Long cashboxId) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAllByCashboxId(cashboxId);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	@Override
	public List<Receipt> getReceiptList(Long reportId, Long cashboxId) {
		List<Receipt> receiptList = new ArrayList<>();
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			List<ReceiptRecord> list = receiptDAO.findAllByZReportId(reportId, cashboxId);
			fillList(receiptList, list, connection.open());
			LOGGER.debug(receiptList);
			return receiptList;
		}
	}
	
	private void fillList(List<Receipt> receipts, List<ReceiptRecord> records, Connection connection) {
		records.forEach(record -> {
			setReferences(record, connection);
			Receipt receipt = new Receipt(record);
			setReferences(receipt, record);
			receipts.add(receipt);
		});
	}
	
	@Override
	public Receipt findOne(Long receiptId) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			ProductService productService = getServiceFactory().getProductService();
			SettingsService settingsService = getServiceFactory().getSettingsService();
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
	public long getCount() {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			return receiptDAO.getCount();
		}
	}
	
	
	private boolean update(Receipt receipt, boolean updateStock, boolean increase) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			
			connection.beginTransaction();
			
			ReceiptDAO receiptDAO = daoFactory.getReceiptDAO(connection.open());
			ReceiptRecord record = receipt.getRecord();
			
			if (receiptDAO.update(record)) {
				ReceiptRecord updated = receiptDAO.findOne(record.getId());
				updateReferences(updated, receipt.getProducts(), updateStock, increase, connection.open());
				setReferences(updated, connection.open());
				receipt.setRecord(updated);
				connection.commit();
				return true;
			}
			return false;
		}
	}
	
	private void updateReferences(ReceiptRecord record, List<Product> products,
	                              boolean updateStock, boolean increase, Connection connection) {
		ReceiptProductDAO receiptProductDAO = daoFactory.getReceiptProductDAO(connection);
		boolean createRefs = receiptProductDAO.findAllByReceiptId(record.getId()).isEmpty();
		
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
		ProductDAO productDAO = daoFactory.getProductDAO(connection);
		Product inStock = productDAO.findOne(product.getId());
		
		if (inStock != null) {
			if (increase) {
				inStock.addQuantity(product.getQuantity());
			} else {
				inStock.addQuantity(product.getQuantity().negate());
			}
			productDAO.update(inStock);
		}
	}
	
	private void setReferences(Receipt receipt, ReceiptRecord record) {
		ProductService productService = getServiceFactory().getProductService();
		SettingsService settingsService = getServiceFactory().getSettingsService();
		receipt.setProducts(productService.findAllByReceiptId(record.getId()));
		receipt.setCategories(settingsService.getTaxCatList());
	}
	
	private void setReferences(ReceiptRecord record, Connection connection) {
		CashboxDAO cashboxDAO = daoFactory.getCashboxDao(connection);
		PaymentTypeDAO paymentTypeDAO = daoFactory.getPaymentTypeDAO(connection);
		ReceiptTypeDAO receiptTypeDAO = daoFactory.getReceiptTypeDAO(connection);
		UserDAO userDAO = daoFactory.getUserDAO(connection);
		ReceiptProductDAO receiptProductDAO = daoFactory.getReceiptProductDAO(connection);
		
		record.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
		record.setPaymentType(paymentTypeDAO.findOne(record.getPaymentTypeId()));
		record.setReceiptType(receiptTypeDAO.findOne(record.getReceiptTypeId()));
		record.setUserCreateBy(userDAO.findOne(record.getCreatedBy()));
		record.setProducts(receiptProductDAO.findAllByReceiptId(record.getId()));
	}
}
