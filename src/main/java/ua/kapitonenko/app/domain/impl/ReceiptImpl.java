package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.System.lineSeparator;

/**
 * Implementation of {@code Receipt} interface.
 */
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
	
	/**
	 * Constructor instantiate the {@code Receipt} with the given {@code ReceiptRecord}.
	 * Is used only by {@link ua.kapitonenko.app.domain.ModelFactory}.
	 */
	ReceiptImpl(ReceiptRecord record) {
		this.record = record;
	}
	
	/**
	 * Constructor instantiate the {@code Receipt} and {@code ReceiptRecord} with the given attributes.
	 * Is used only by {@link ua.kapitonenko.app.domain.ModelFactory}.
	 */
	ReceiptImpl(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		record = new ReceiptRecord(null, cashboxId, paymentTypeId, receiptTypeId, cancelled, createdBy);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getTypeId() {
		return record.getReceiptTypeId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocalId(Long localId) {
		this.localId = localId;
		for (Product product : products) {
			product.setLocaleId(localId);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Changes the quantity if list contains the product.
	 * Otherwise adds product to the list and sets product locale id to active locale.
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
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
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
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
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTotalCost() {
		return products.stream()
				       .map(Product::getCost)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTaxAmount() {
		return products.stream()
				       .map(Product::getTax)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	/**
	 * {@inheritDoc}
	 * Only cash fiscal receipts can be returned.
	 */
	@Override
	public boolean isReturnAllowed() {
		return !record.getReceiptTypeId().equals(Application.Ids.RECEIPT_TYPE_RETURN.getValue())
				       && record.getPaymentTypeId().equals(Application.Ids.PAYMENT_TYPE_CASH.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * PaymentType has to be defined and list of products has to contain items.
	 */
	@Override
	public boolean isPrintAllowed() {
		return !record.getPaymentTypeId().equals(Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue())
				       && !products.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCreatedAt() {
		return record.getCreatedAt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId() {
		return record.getId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		return record.isCancelled();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReceiptType(ReceiptType type) {
		this.receiptType = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReceiptType getReceiptType() {
		return receiptType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCategories(List<TaxCategory> taxCats) {
		this.categories = taxCats;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Product> getProducts() {
		return products;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReceiptRecord getRecord() {
		return record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	/**
	 * {@inheritDoc}
	 * for {@code Receipt} and {@code ReceiptRecord}.
	 */
	@Override
	public void setPaymentType(PaymentType type) {
		paymentType = type;
		record.setPaymentTypeId(type.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRecord(ReceiptRecord record) {
		this.record = record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getLocalId() {
		return localId;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ReceiptImpl{")
				       .append("record=").append(record).append(lineSeparator())
				       .append("            paymentType=").append(paymentType).append(lineSeparator())
				       .append("            receiptType=").append(receiptType).append(lineSeparator())
				       .append("            cashbox=").append(cashbox).append(lineSeparator())
				       .append("            products=").append(products).append(lineSeparator())
				       .append("            localId=").append(localId).append(lineSeparator())
				       .append("            taxByCategory=").append(getTaxByCategory()).append(lineSeparator())
				       .append("            costByCategory=").append(getCostByCategory())
				       .append("}")
				       .toString();
	}
}
