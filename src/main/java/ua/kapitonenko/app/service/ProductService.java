package ua.kapitonenko.app.service;

import ua.kapitonenko.app.domain.records.Product;

import java.util.List;

public interface ProductService {
	Product createProduct(Product product);
	
	List<Product> getProductsList();
	
	List<Product> getProductsList(int offset, int limit, Long localeId);
	
	List<Product> findByIdOrName(Long localeId, Long productId, String name);
	
	List<Product> findAllByReceiptId(Long id);
	
	long getProductsCount();
	
	boolean delete(Long prodId, Long userId);
}
