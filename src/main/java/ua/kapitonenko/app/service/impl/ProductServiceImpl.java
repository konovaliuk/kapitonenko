package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.connection.ConnectionPool;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.tables.ProductsTable;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ProductLocale;
import ua.kapitonenko.app.exceptions.DAOException;
import ua.kapitonenko.app.service.ProductService;

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
	public List<Product> getProductsList(int offset, int limit, Long localeId) {
		Connection connection = pool.getConnection();
		try {
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			List<Product> products = productDAO.findAllByQuery("ORDER BY " + ProductsTable.ID +
					                                                   " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
				
			});
			products.forEach(product -> {
				setReferences(product, connection);
				product.setLocaleId(localeId);
			});
			
			return products;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Product> findByIdOrName(Long localeId, Long productId, String name) {
		LOGGER.debug(name);
		Connection connection = pool.getConnection();
		try {
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			List<Product> products = productDAO.findByIdOrName(localeId, productId, name);
			products.forEach(product -> setReferences(product, connection));
			
			return products;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Product> findAllByReceiptId(Long id) {
		Connection connection = pool.getConnection();
		try {
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			List<Product> products = productDAO.findAllByReceiptId(id);
			LOGGER.debug(products);
			products.forEach(product -> setReferences(product, connection));
			
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
	
	@Override
	public long getProductsCount() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			return productDAO.getCount();
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public boolean delete(Long prodId, Long userId) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			Product product = productDAO.findOne(prodId);
			return productDAO.delete(product, userId);
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public boolean update(Product product) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			return productDAO.update(product);
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public Product findOne(Long id) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ProductDAO productDAO = Application.getDAOFactory().getProductDAO(connection);
			return productDAO.findOne(id);
		} finally {
			pool.close(connection);
		}
	}
	
	
}
