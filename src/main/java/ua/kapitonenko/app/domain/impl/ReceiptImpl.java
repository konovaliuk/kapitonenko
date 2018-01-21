package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.records.*;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.domain.Receipt;

import java.math.BigDecimal;
import java.util.*;

public class ReceiptImpl implements Receipt {
	
	private ReceiptRecord record;
	private PaymentType paymentType;
	private ReceiptType receiptType;
	private Cashbox cashbox;
	private List<Product> products = new ArrayList<>();
	private Long localId;
	private List<TaxCategory> categories;
	private Map<TaxCategory, BigDecimal> taxByCategory = new HashMap<>();
	private Map<TaxCategory, BigDecimal> costByCategory = new HashMap<>();
	
	
	ReceiptImpl(ReceiptRecord record) {
		this.record = record;
	}
	
	ReceiptImpl(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		record = new ReceiptRecord(null, cashboxId, paymentTypeId, receiptTypeId, cancelled, createdBy);
	}
	
	@Override
	public Long getTypeId() {
		return record.getReceiptTypeId();
	}
	
	@Override
	public void setLocalId(Long localId) {
		this.localId = localId;
		for (Product product : products) {
			product.setLocaleId(localId);
		}
	}
	
	@Override
	public void addProduct(Product product) {
		if (products.contains(product)) {
			Product existing = products.get(products.indexOf(product));
			existing.addQuantity(product.getQuantity());
		} else {
			product.setLocaleId(localId);
			products.add(product);
		}
	}
	
	@Override
	public void remove(Long productId) {
		Product found = null;
		for (Product product : products) {
			if (product.getId().equals(productId)) {
				found = product;
			}
		}
		products.remove(found);
	}
	
	@Override
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
	
	@Override
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
	
	
	@Override
	public BigDecimal getTotalCost() {
		return products.stream()
				       .map(Product::getCost)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	@Override
	public BigDecimal getTaxAmount() {
		return products.stream()
				       .map(Product::getTax)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	@Override
	public boolean isReturnAllowed() {
		return !record.getReceiptTypeId().equals(Application.Ids.RECEIPT_TYPE_RETURN.getValue())
				       && record.getPaymentTypeId().equals(Application.Ids.PAYMENT_TYPE_CASH.getValue());
	}
	
	@Override
	public boolean isPrintAllowed() {
		return !record.getPaymentTypeId().equals(Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue())
				       && !products.isEmpty();
	}
	
	@Override
	public Date getCreatedAt() {
		return record.getCreatedAt();
	}
	
	@Override
	public Long getId() {
		return record.getId();
	}
	
	@Override
	public boolean isCancelled() {
		return record.isCancelled();
	}
	
	@Override
	public void setReceiptType(ReceiptType type) {
		this.receiptType = type;
	}
	
	@Override
	public ReceiptType getReceiptType() {
		return receiptType;
	}
	
	@Override
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	@Override
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	@Override
	public void setCategories(List<TaxCategory> taxCats) {
		this.categories = taxCats;
	}
	
	@Override
	public List<Product> getProducts() {
		return products;
	}
	
	@Override
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Override
	public ReceiptRecord getRecord() {
		return record;
	}
	
	@Override
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	@Override
	public void setPaymentType(PaymentType type) {
		paymentType = type;
	}
	
	@Override
	public void setRecord(ReceiptRecord record) {
		this.record = record;
	}
	
	@Override
	public Long getLocalId() {
		return localId;
	}
	
	@Override
	public String toString() {
		if (record != null) {
			return record.toString();
		}
		return super.toString();
	}
	
}
