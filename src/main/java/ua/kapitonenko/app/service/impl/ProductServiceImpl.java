package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.*;
import ua.kapitonenko.app.dao.records.LocaleRecord;
import ua.kapitonenko.app.dao.records.ProductLocale;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.dao.tables.ProductsTable;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.service.ProductService;
import ua.kapitonenko.app.service.SettingsService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl extends BaseService implements ProductService {
	
	ProductServiceImpl() {
	}
	
	@Override
	public Product createProduct(Product product) {
		
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			connection.beginTransaction();
			
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			ProductLocaleDAO productLocaleDAO = getDaoFactory().getProductLocaleDAO(connection.open());
			
			productDAO.insert(product.getRecord());
			
			ProductRecord created = productDAO.findOne(product.getRecord().getId());
			product.setRecord(created);
			product.getNames().forEach(pl -> {
				pl.setProductId(product.getRecord().getId());
				productLocaleDAO.insert(pl);
			});
			connection.commit();
			return product;
		}
	}
	
	@Override
	public List<Product> getProductsList(int offset, int limit, Long localeId) {
		List<Product> products = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			List<ProductRecord> records = productDAO.findAllByQuery("ORDER BY " + ProductsTable.ID +
					                                                        " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
				
			});
			records.forEach(record -> {
				Product product = initProduct(record, connection.open());
				product.setLocaleId(localeId);
				products.add(product);
			});
			return products;
		}
	}
	
	@Override
	public Product findOne(Long id) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			return initProduct(productDAO.findOne(id), connection.open());
		}
	}
	
	@Override
	public List<Product> findByIdOrName(Long localeId, Long productId, String name) {
		List<Product> products = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			List<ProductRecord> records = productDAO.findByIdOrName(localeId, productId, name);
			records.forEach(record -> products.add(initProduct(record, connection.open())));
			return products;
		}
	}
	
	@Override
	public List<Product> findAllByReceiptId(Long id) {
		List<Product> products = new ArrayList<>();
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			List<ProductRecord> records = productDAO.findAllByReceiptId(id);
			records.forEach(record -> products.add(initProduct(record, connection.open())));
			return products;
		}
	}
	
	private Product initProduct(ProductRecord record, Connection connection) {
		Product product = getModelFactory().createProduct(record);
		
		ProductLocaleDAO productLocaleDAO = getDaoFactory().getProductLocaleDAO(connection);
		TaxCategoryDAO taxCategoryDAO = getDaoFactory().getTaxCategoryDAO(connection);
		UnitDAO unitDAO = getDaoFactory().getUnitDAO(connection);
		LocaleDAO localeDAO = getDaoFactory().getLocaleDAO(connection);
		
		List<ProductLocale> lang = productLocaleDAO.findByProductAndKey(record.getId(), Keys.PRODUCT_NAME);
		lang.forEach(pl -> pl.setLocale(localeDAO.findOne(pl.getLocaleId())));
		product.setNames(lang);
		
		product.setTaxCategory(taxCategoryDAO.findOne(record.getTaxCategoryId()));
		product.setUnit(unitDAO.findOne(record.getUnitId()));
		
		return product;
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			return productDAO.getCount();
		}
	}
	
	@Override
	public boolean delete(Long prodId, Long userId) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			ProductRecord product = productDAO.findOne(prodId);
			return productDAO.delete(product, userId);
		}
	}
	
	@Override
	public boolean update(Product product) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ProductDAO productDAO = getDaoFactory().getProductDAO(connection.open());
			return productDAO.update(product.getRecord());
		}
	}

	
	@Override
	public Product newProduct() {
		SettingsService settingsService = getServiceFactory().getSettingsService();
		List<LocaleRecord> locales = settingsService.getLocaleList();
		
		ProductRecord productRecord = new ProductRecord();
		Product product = getModelFactory().createProduct(productRecord);
		product.initNames(locales);
		return product;
	}
}
