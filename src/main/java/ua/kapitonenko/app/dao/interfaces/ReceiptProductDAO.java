package ua.kapitonenko.app.dao.interfaces;


import ua.kapitonenko.app.domain.records.ReceiptProduct;

import java.util.List;

public interface ReceiptProductDAO extends DAO<ReceiptProduct> {
	List<ReceiptProduct> findAllByReceiptId(Long receiptId);
}