package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.dao.records.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Receipt extends Model {
	Long getTypeId();
	
	void setProducts(List<Product> products);
	
	ReceiptRecord getRecord();
	
	PaymentType getPaymentType();
	
	void setPaymentType(PaymentType type);
	
	void setRecord(ReceiptRecord record);
	
	Long getLocalId();
	
	void setLocalId(Long localId);
	
	List<Product> getProducts();
	
	Map<TaxCategory, BigDecimal> getTaxByCategory();
	
	Map<TaxCategory, BigDecimal> getCostByCategory();
	
	void setCategories(List<TaxCategory> taxCats);
	
	BigDecimal getTotalCost();
	
	BigDecimal getTaxAmount();
	
	void addProduct(Product product);
	
	void remove(Long productId);
	
	boolean isReturnAllowed();
	
	boolean isPrintAllowed();
	
	Date getCreatedAt();
	
	Long getId();
	
	boolean isCancelled();
	
	void setReceiptType(ReceiptType type);
	
	ReceiptType getReceiptType();
	
	void setCashbox(Cashbox one);
	
	Cashbox getCashbox();
}
