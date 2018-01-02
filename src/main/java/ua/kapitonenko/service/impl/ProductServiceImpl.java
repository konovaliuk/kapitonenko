package ua.kapitonenko.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.dao.interfaces.*;
import ua.kapitonenko.domain.entities.Product;
import ua.kapitonenko.domain.entities.ProductLocale;
import ua.kapitonenko.exceptions.DAOException;
import ua.kapitonenko.service.ProductService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
	private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class);
	private static ProductServiceImpl instance = new ProductServiceImpl();
	private ConnectionPool pool = Application.getConnectionPool();
	
	private ProductServiceImpl() {
	}
	
	public static ProductServiceImpl getInstance() {
		return instance;
	}
	
	@Override
	public Product createProduct(Product product) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			connection.setAutoCommit(false);
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			ProductLocaleDAO productLocaleDAO = Application.getDAOFactory().getProductLocaleDAO(connection);
			productDAO.insert(product);
			Product created = productDAO.findOne(product.getId());
			product.getNames().forEach(pl -> {
				pl.setProductId(product.getId());
				productLocaleDAO.insert(pl);
			});
			connection.commit();
			return created;
			
		} catch (SQLException e) {
			try {
				connection.rollback();
				return null;
			} catch (SQLException e1) {
				throw new DAOException(e);
			}
			
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Product> getProductsList() {
		Connection connection = pool.getConnection();
		try {
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			List<Product> products = productDAO.findAll();
			products.forEach(product -> {
				setReferences(product, connection);
			});
			
			return products;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Product> findByIdOrName(Long localeId, Long productId, String name) {
		Connection connection = pool.getConnection();
		try {
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			List<Product> products = productDAO.findByIdOrName(localeId, productId, name);
			products.forEach(product -> {
				setReferences(product, connection);
			});
			
			return products;
		} finally {
			pool.close(connection);
		}
	}
	
	private void setReferences(Product product, Connection connection) {
		ProductLocaleDAO productLocaleDAO = Application.getDAOFactory().getProductLocaleDAO(connection);
		TaxCategoryDAO taxCategoryDAO = Application.getDAOFactory().getTaxCategoryDAO(connection);
		UnitDAO unitDAO = Application.getDAOFactory().getUnitDAO(connection);
		LocaleDAO localeDAO = Application.getDAOFactory().getLocaleDAO(connection);
		
		List<ProductLocale> lang = productLocaleDAO.findByProductAndKey(product.getId(), Keys.PRODUCT_NAME);
		lang.forEach(pl -> pl.setLocale(localeDAO.findOne(pl.getLocaleId())));
		product.setNames(lang);
		product.setTaxCategory(taxCategoryDAO.findOne(product.getTaxCategoryId()));
		product.setUnit(unitDAO.findOne(product.getUnitId()));
	}
	
}
