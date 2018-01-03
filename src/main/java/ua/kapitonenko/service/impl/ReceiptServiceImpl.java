package ua.kapitonenko.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.dao.interfaces.*;
import ua.kapitonenko.dao.tables.ReceiptsTable;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.entities.Receipt;
import ua.kapitonenko.domain.entities.ReceiptProduct;
import ua.kapitonenko.exceptions.DAOException;
import ua.kapitonenko.service.ProductService;
import ua.kapitonenko.service.ReceiptService;

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
	
	private void setReferences(Receipt record, Connection connection) {
		CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection);
		PaymentTypeDAO paymentTypeDAO = Application.getDAOFactory().getPaymentTypeDAO(connection);
		ReceiptTypeDAO receiptTypeDAO = Application.getDAOFactory().getReceiptTypeDAO(connection);
		UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
		ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
		
		record.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
		record.setPaymentType(paymentTypeDAO.findOne(record.getPaymentTypeId()));
		record.setReceiptType(receiptTypeDAO.findOne(record.getReceiptTypeId()));
		record.setPaymentType(paymentTypeDAO.findOne(record.getPaymentTypeId()));
		record.setUserCreateBy(userDAO.findOne(record.getCreatedBy()));
		record.setProducts(receiptProductDAO.findAllByReceiptId(record.getId()));
	}
	
	
	@Override
	public boolean update(ReceiptCalculator calculator) {
		
		Connection connection = null;
		try {
			connection = pool.getConnection();
			connection.setAutoCommit(false);
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
			
			Receipt receipt = calculator.getReceipt();
			
			if (receiptDAO.update(receipt)) {
				
				Receipt created = receiptDAO.findOne(receipt.getId());
				calculator.getProducts().forEach(product -> {
					
					ReceiptProduct rp = new ReceiptProduct(null, created.getId(), product.getId(), product.getQuantity());
					receiptProductDAO.insert(rp);
				});
				setReferences(created, connection);
				calculator.setReceipt(created);
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
	public boolean create(ReceiptCalculator calculator) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			
			Receipt receipt = calculator.getReceipt();
			if (receiptDAO.insert(receipt)) {
				Receipt created = receiptDAO.findOne(receipt.getId());
				setReferences(created, connection);
				calculator.setReceipt(created);
				return true;
			}
			return false;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<ReceiptCalculator> getReceiptList(int offset, int limit, Long cashboxId) {
		List<ReceiptCalculator> calculatorList = new ArrayList<>();
		Connection connection = pool.getConnection();
		try {
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ProductService productService = Application.getServiceFactory().getProductService();
			List<Receipt> list;
			
			if (cashboxId == null) {
				list = receiptDAO.findAllByQuery("ORDER BY ? LIMIT ? OFFSET ?", ps -> {
					ps.setString(1, ReceiptsTable.ID);
					ps.setInt(3, offset);
					ps.setInt(2, limit);
				});
			} else {
				list = receiptDAO.findAllByQuery("WHERE ?=? ORDER BY ? LIMIT ? OFFSET ?", ps -> {
					ps.setString(1, ReceiptsTable.CASHBOX_ID);
					ps.setLong(2, cashboxId);
					ps.setInt(4, offset);
					ps.setInt(3, limit);
				});
			}
			
			list.forEach(receipt -> {
				setReferences(receipt, connection);
				ReceiptCalculator calculator = new ReceiptCalculator();
				calculator.setReceipt(receipt);
				calculator.setProducts(productService.findAllByReceiptId(receipt.getId()));
				calculatorList.add(calculator);
			});
			return calculatorList;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public Receipt findById(Long receiptId) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			Receipt receipt = receiptDAO.findOne(receiptId);
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
			Receipt receipt = receiptDAO.findOne(receiptId);
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
	public int getReceiptsCount() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			return receiptDAO.getCount();
		} finally {
			pool.close(connection);
		}
	}
}
