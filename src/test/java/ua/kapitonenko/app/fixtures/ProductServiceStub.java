package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.persistence.dao.DAOFactory;
import ua.kapitonenko.app.service.ProductService;

import java.util.List;

public class ProductServiceStub implements ProductService {
	
	@Override
	public Product createProduct(Product product) {
		return null;
	}
	
	@Override
	public List<Product> getProductsList(int offset, int limit, Long localeId) {
		return null;
	}
	
	@Override
	public List<Product> findByIdOrName(Long localeId, Long productId, String name) {
		return null;
	}
	
	@Override
	public List<Product> findAllByReceiptId(Long id) {
		return null;
	}
	
	@Override
	public long getCount() {
		return 0;
	}
	
	@Override
	public boolean delete(Long prodId, Long userId) {
		return false;
	}
	
	@Override
	public boolean update(Product product) {
		return false;
	}
	
	@Override
	public Product findOne(Long id) {
		return null;
	}
	
	@Override
	public Product newProduct() {
		return null;
	}
	
	@Override
	public void setDaoFactory(DAOFactory daoFactory) {
	
	}
}
