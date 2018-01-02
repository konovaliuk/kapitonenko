package ua.kapitonenko.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.dao.interfaces.*;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.entities.Receipt;
import ua.kapitonenko.domain.entities.ReceiptProduct;
import ua.kapitonenko.exceptions.DAOException;
import ua.kapitonenko.service.ReceiptService;

import java.sql.Connection;
import java.sql.SQLException;

public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger LOGGER = Logger.getLogger(ReceiptService.class);
	
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
		record.setProducts(receiptProductDAO.findAll());
	}
	
	
	@Override
	public boolean updateReceipt(ReceiptCalculator calculator) {
		LOGGER.debug("update receipt method");
		Connection connection = null;
		try {
			connection = pool.getConnection();
			connection.setAutoCommit(false);
			ReceiptDAO receiptDAO = Application.getDAOFactory().getReceiptDAO(connection);
			ReceiptProductDAO receiptProductDAO = Application.getDAOFactory().getReceiptProductDAO(connection);
			
			Receipt receipt = calculator.getReceipt();
			
			LOGGER.debug(receipt);
			if (receiptDAO.update(receipt)) {
				LOGGER.debug("updated receipt");
				Receipt created = receiptDAO.findOne(receipt.getId());
				calculator.getProducts().forEach(product -> {
					LOGGER.debug("for each");
					ReceiptProduct rp = new ReceiptProduct(
							                                      null, created.getId(), product.getId(), product.getQuantity());
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
	public boolean createReceipt(ReceiptCalculator calculator) {
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
}
