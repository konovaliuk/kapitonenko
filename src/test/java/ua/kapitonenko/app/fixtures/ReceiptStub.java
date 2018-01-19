package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.dao.records.*;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.domain.Receipt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReceiptStub implements Receipt {
	
	private ReceiptRecord receiptRecord;
	private BigDecimal totalCost;
	private BigDecimal taxAmount;
	private PaymentType paymentType;
	private Map<TaxCategory, BigDecimal> costByCat;
	private Map<TaxCategory, BigDecimal> taxByCat;
	private List<Product> products;
	
	
	public ReceiptStub(ReceiptRecord record) {
		receiptRecord = record;
	}
	
	@Override
	public Long getTypeId() {
		return receiptRecord.getReceiptTypeId();
	}
	
	@Override
	public ReceiptRecord getRecord() {
		return receiptRecord;
	}
	
	@Override
	public void setRecord(ReceiptRecord record) {
		receiptRecord = record;
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
	public List<Product> getProducts() {
		return products;
	}
	
	@Override
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Override
	public Map<TaxCategory, BigDecimal> getTaxByCategory() {
		return taxByCat;
	}
	
	public void setTaxByCat(Map<TaxCategory, BigDecimal> map) {
		taxByCat = map;
	}
	
	@Override
	public Map<TaxCategory, BigDecimal> getCostByCategory() {
		return costByCat;
	}
	
	public void setCostByCat(Map<TaxCategory, BigDecimal> map) {
		costByCat = map;
	}
	
	@Override
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(BigDecimal amount) {
		totalCost = amount;
	}
	
	@Override
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	
	public void setTaxAmount(BigDecimal amount) {
		taxAmount = amount;
	}
	
	@Override
	public Long getLocalId() {
		return null;
	}
	
	@Override
	public void setLocalId(Long localId) {
	
	}
	
	@Override
	public void addProduct(Product product) {
	
	}
	
	@Override
	public void setCategories(List<TaxCategory> taxCats) {
	
	}
	
	@Override
	public void remove(Long productId) {
	
	}
	
	@Override
	public boolean isReturnAllowed() {
		return false;
	}
	
	@Override
	public boolean isPrintAllowed() {
		return false;
	}
	
	@Override
	public Date getCreatedAt() {
		return null;
	}
	
	@Override
	public Long getId() {
		return null;
	}
	
	@Override
	public boolean isCancelled() {
		return false;
	}
	
	@Override
	public void setReceiptType(ReceiptType type) {
	
	}
	
	@Override
	public ReceiptType getReceiptType() {
		return null;
	}
	
	@Override
	public void setCashbox(Cashbox one) {
	
	}
	
	@Override
	public Cashbox getCashbox() {
		return null;
	}
}
