package ua.kapitonenko.app.service;

import ua.kapitonenko.app.domain.Product;

import java.util.List;

public interface ProductService extends Service {
	
	Product createProduct(Product product);
	
	List<Product> getProductsList(int offset, int limit, Long localeId);
	
	List<Product> findByIdOrName(Long localeId, Long productId, String name);
	
	List<Product> findAllByReceiptId(Long id);
	
	long getCount();
	
	boolean delete(Long prodId, Long userId);
	
	boolean update(Product product);
	
	Product findOne(Long id);
	
	Product newProduct();
}
