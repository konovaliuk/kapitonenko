package ua.kapitonenko.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.dao.interfaces.*;
import ua.kapitonenko.domain.Product;
import ua.kapitonenko.domain.ProductLocale;
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
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			ProductLocaleDAO productLocaleDAO = Application.getDAOFactory().getProductLocaleDAO(connection);
			TaxCategoryDAO taxCategoryDAO = Application.getDAOFactory().getTaxCategoryDAO(connection);
			UnitDAO unitDAO = Application.getDAOFactory().getUnitDAO(connection);
			LocaleDAO localeDAO = Application.getDAOFactory().getLocaleDAO(connection);
			
			List<Product> products = productDAO.findAll();
			products.forEach(product -> {
				List<ProductLocale> lang = productLocaleDAO.findByProductAndKey(product.getId(), Keys.PRODUCT_NAME);
				lang.forEach(pl -> pl.setLocale(localeDAO.findOne(pl.getLocaleId())));
				product.setNames(lang);
				product.setTaxCategory(taxCategoryDAO.findOne(product.getTaxCategoryId()));
				product.setUnit(unitDAO.findOne(product.getUnitId()));
			});
			
			return products;
		} finally {
			pool.close(connection);
		}
	}
	
}
