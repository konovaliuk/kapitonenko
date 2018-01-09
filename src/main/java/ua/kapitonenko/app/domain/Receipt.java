package ua.kapitonenko.app.domain;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.records.Product;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.domain.records.TaxCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt extends Model implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Receipt.class);
	
	private ReceiptRecord record;
	private List<Product> products = new ArrayList<>();
	private Long localId;

	private List<TaxCategory> categories;
	private Map<TaxCategory, BigDecimal> taxByCategory = new HashMap<>();
	private Map<TaxCategory, BigDecimal> costByCategory = new HashMap<>();
	
	public Receipt() {
	}
	
	public Receipt(ReceiptRecord record) {
		this.record = record;
	}
	
	public ReceiptRecord getRecord() {
		return record;
	}
	
	public void setRecord(ReceiptRecord record) {
		this.record = record;
	}
	
	public Long getLocalId() {
		return localId;
	}
	
	public void setLocalId(Long localId) {
		this.localId = localId;
		for (Product product : products) {
			product.setLocaleId(localId);
		}
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		if (products.contains(product)) {
			Product existing = products.get(products.indexOf(product));
			existing.addQuantity(product.getQuantity());
		} else {
			product.setLocaleId(localId);
			products.add(product);
		}
	}
	
	public Map<TaxCategory, BigDecimal> getTaxByCategory() {
		for (TaxCategory cat : categories) {
			taxByCategory.put(cat, new BigDecimal("0.00"));
		}
		
		for (Product product : products) {
			TaxCategory cat = product.getTaxCategory();
			taxByCategory.put(cat, taxByCategory.get(cat).add(product.getTax()));
		}
		return taxByCategory;
	}
	
	public Map<TaxCategory, BigDecimal> getCostByCategory() {
		for (TaxCategory cat : categories) {
			costByCategory.put(cat, new BigDecimal("0.00"));
		}
		
		for (Product product : products) {
			TaxCategory cat = product.getTaxCategory();
			costByCategory.put(cat, costByCategory.get(cat).add(product.getCost()));
		}
		return costByCategory;
	}
	
	public void setCategories(List<TaxCategory> taxCats) {
		this.categories = taxCats;
	}
	
	public BigDecimal getTotalCost() {
		return products.stream()
				       .map(Product::getCost)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
		
	}
	
	public BigDecimal getTaxAmount() {
		return products.stream()
				       .map(Product::getTax)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	public void remove(Long productId) {
		Product found = null;
		for (Product product : products) {
			if (product.getId().equals(productId)) {
				found = product;
			}
		}
		products.remove(found);
	}
	
	public boolean isReturnVisible() {
		return !record.getReceiptTypeId().equals(Application.getId(Application.RECEIPT_TYPE_RETURN))
				       && record.getPaymentTypeId().equals(Application.getId(Application.PAYMENT_TYPE_CASH));
	}
	
	
}
