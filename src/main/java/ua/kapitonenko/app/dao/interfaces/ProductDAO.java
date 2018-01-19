package ua.kapitonenko.app.dao.interfaces;

import ua.kapitonenko.app.dao.records.ProductRecord;

import java.util.List;

public interface ProductDAO extends DAO<ProductRecord> {
	
	List<ProductRecord> findByIdOrName(Long localeId, Long productId, String name);
	
	List<ProductRecord> findAllByReceiptId(Long receiptId);
}