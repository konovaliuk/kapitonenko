package ua.kapitonenko.service;

import ua.kapitonenko.domain.entities.Product;

import java.util.List;

public interface ProductService {
	Product createProduct(Product product);
	
	List<Product> getProductsList();
	
	List<Product> getProductsList(int offset, int limit);
	
	List<Product> findByIdOrName(Long localeId, Long productId, String name);
	
	List<Product> findAllByReceiptId(Long id);
	
	int getProductsCount();
	
	boolean delete(Long prodId, Long userId);
}
