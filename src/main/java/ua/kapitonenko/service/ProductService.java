package ua.kapitonenko.service;

import ua.kapitonenko.domain.entities.Product;

import java.util.List;

public interface ProductService {
	Product createProduct(Product product);
	
	List<Product> getProductsList();
	
	List<Product> findByIdOrName(Long localeId, Long productId, String name);
}
