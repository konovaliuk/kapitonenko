package ua.kapitonenko.service;

import ua.kapitonenko.domain.Product;

import java.util.List;

public interface ProductService {
	Product createProduct(Product product);
	
	List<Product> getProductsList();
}
