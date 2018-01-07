package ua.kapitonenko.app.dao.interfaces;

import ua.kapitonenko.app.domain.records.Product;

import java.util.List;

public interface ProductDAO extends DAO<Product> {
	
	List<Product> findByIdOrName(Long localeId, Long productId, String name);
	
	List<Product> findAllByReceiptId(Long receiptId);
}