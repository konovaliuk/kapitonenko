package ua.kapitonenko.app.persistence.dao;


import ua.kapitonenko.app.persistence.records.ReceiptProduct;

import java.util.List;

public interface ReceiptProductDAO extends DAO<ReceiptProduct> {
	List<ReceiptProduct> findAllByReceiptId(Long receiptId);
}