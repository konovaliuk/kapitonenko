package ua.kapitonenko.app.persistence.dao;

import ua.kapitonenko.app.persistence.records.ProductRecord;

import java.util.List;

public interface ProductDAO extends DAO<ProductRecord> {
	
	List<ProductRecord> findByIdOrName(Long localeId, Long productId, String name);
	
	List<ProductRecord> findAllByReceiptId(Long receiptId);
}