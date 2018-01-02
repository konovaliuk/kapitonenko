package ua.kapitonenko.domain;

import org.apache.log4j.Logger;
import ua.kapitonenko.domain.entities.PaymentType;
import ua.kapitonenko.domain.entities.Product;
import ua.kapitonenko.domain.entities.Receipt;
import ua.kapitonenko.domain.entities.TaxCategory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptCalculator {
	private static final Logger LOGGER = Logger.getLogger(ReceiptCalculator.class);
	List<PaymentType> paymentTypes;
	private Receipt receipt;
	private List<Product> products = new ArrayList<>();
	private Long localId;
	private List<TaxCategory> categories;
	private Map<TaxCategory, BigDecimal> taxByCategory = new HashMap<>();
	private BigDecimal totalCost;
	private BigDecimal taxAmount;
	
	
	public Receipt getReceipt() {
		return receipt;
	}
	
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
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
	
	public void setCategories(List<TaxCategory> taxCats) {
		this.categories = taxCats;
	}
	
	public BigDecimal getTotalCost() {
		return products.stream()
				       .map(Product::getCost)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
		
	}
	
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	
	public BigDecimal getTaxAmount() {
		return taxByCategory.values().stream()
				       .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public List<PaymentType> getPaymentTypes() {
		return paymentTypes;
	}
	
	public void setPaymentTypes(List<PaymentType> paymentTypes) {
		this.paymentTypes = paymentTypes;
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
	
	
}
