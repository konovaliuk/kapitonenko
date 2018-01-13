package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.tables.ProductsTable;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ProductLocale;
import ua.kapitonenko.app.service.ProductService;

import java.sql.Connection;
import java.util.List;

public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class);
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	@Override
	public Product createProduct(Product product) {
		
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			connection.beginTransaction();
			
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			ProductLocaleDAO productLocaleDAO = daoFactory.getProductLocaleDAO(connection.open());
			productDAO.insert(product);
			Product created = productDAO.findOne(product.getId());
			product.getNames().forEach(pl -> {
				pl.setProductId(product.getId());
				productLocaleDAO.insert(pl);
			});
			connection.commit();
			return created;
		}
	}
	
	@Override
	public List<Product> getProductsList() {
		
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			List<Product> products = productDAO.findAll();
			products.forEach(product -> {
				setReferences(product, connection.open());
			});
			return products;
		}
	}
	
	@Override
	public List<Product> getProductsList(int offset, int limit, Long localeId) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			List<Product> products = productDAO.findAllByQuery("ORDER BY " + ProductsTable.ID +
					                                                   " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
				
			});
			products.forEach(product -> {
				setReferences(product, connection.open());
				product.setLocaleId(localeId);
			});
			return products;
		}
	}
	
	@Override
	public List<Product> findByIdOrName(Long localeId, Long productId, String name) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			List<Product> products = productDAO.findByIdOrName(localeId, productId, name);
			products.forEach(product -> setReferences(product, connection.open()));
			return products;
		}
	}
	
	@Override
	public List<Product> findAllByReceiptId(Long id) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			List<Product> products = productDAO.findAllByReceiptId(id);
			LOGGER.debug(products);
			products.forEach(product -> setReferences(product, connection.open()));
			return products;
		}
	}
	
	private void setReferences(Product product, Connection connection) {
		ProductLocaleDAO productLocaleDAO = daoFactory.getProductLocaleDAO(connection);
		TaxCategoryDAO taxCategoryDAO = daoFactory.getTaxCategoryDAO(connection);
		UnitDAO unitDAO = daoFactory.getUnitDAO(connection);
		LocaleDAO localeDAO = daoFactory.getLocaleDAO(connection);
		
		List<ProductLocale> lang = productLocaleDAO.findByProductAndKey(product.getId(), Keys.PRODUCT_NAME);
		lang.forEach(pl -> pl.setLocale(localeDAO.findOne(pl.getLocaleId())));
		product.setNames(lang);
		product.setTaxCategory(taxCategoryDAO.findOne(product.getTaxCategoryId()));
		product.setUnit(unitDAO.findOne(product.getUnitId()));
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			return productDAO.getCount();
		}
	}
	
	@Override
	public boolean delete(Long prodId, Long userId) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			Product product = productDAO.findOne(prodId);
			return productDAO.delete(product, userId);
		}
	}
	
	@Override
	public boolean update(Product product) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			return productDAO.update(product);
		}
	}
	
	@Override
	public Product findOne(Long id) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ProductDAO productDAO = daoFactory.getProductDAO(connection.open());
			return productDAO.findOne(id);
		}
	}
}
