package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@code Receipt} interface defines methods for working with {@link ReceiptRecord} and all its dependencies.
 */
public interface Receipt extends Model {
	
	/**
	 * Returns {@link ReceiptType} record id.
	 */
	Long getTypeId();
	
	/**
	 * Returns the list of {@code Product}.
	 */
	List<Product> getProducts();
	
	/**
	 * Sets the list of {@code Product}.
	 */
	void setProducts(List<Product> products);
	
	/**
	 * Returns the {@code ReceiptRecord}.
	 */
	ReceiptRecord getRecord();
	
	/**
	 * Sets the {@code ReceiptRecord}.
	 */
	void setRecord(ReceiptRecord record);
	
	/**
	 * Returns the {@code PaymentType}.
	 */
	PaymentType getPaymentType();
	
	/**
	 * Sets the {@code PaymentType}.
	 */
	void setPaymentType(PaymentType type);
	
	/**
	 * Returns the current {@link LocaleRecord} id.
	 */
	Long getLocalId();
	
	/**
	 * Sets the current {@link LocaleRecord} id.
	 */
	void setLocalId(Long localId);
	
	/**
	 * The tax amount grouped by {@link TaxCategory} record.
	 */
	Map<TaxCategory, BigDecimal> getTaxByCategory();
	
	/**
	 * The gross total grouped by {@link TaxCategory} record.
	 */
	Map<TaxCategory, BigDecimal> getCostByCategory();
	
	/**
	 * Sets the list of supported {@code TaxCategory}.
	 */
	void setCategories(List<TaxCategory> taxCats);
	
	/**
	 * Returns gross total.
	 */
	BigDecimal getTotalCost();
	
	/**
	 * Returns total tax amount.
	 */
	BigDecimal getTaxAmount();
	
	/**
	 * Adds products to the list.
	 */
	void addProduct(Product product);
	
	/**
	 * Removes products from the list.
	 */
	void remove(Long productId);
	
	/**
	 * Indicates whether return receipt can be created based on this receipt.
	 */
	boolean isReturnAllowed();
	
	/**
	 * Indicates whether receipt contains enough information to print valid receipt document.
	 */
	boolean isPrintAllowed();
	
	/**
	 * Returns the date of creation.
	 */
	Date getCreatedAt();
	
	/**
	 * Returns the {@link ReceiptRecord} id.
	 */
	Long getId();
	
	/**
	 * Returns the status of receipt active(false) of cancelled(true).
	 */
	boolean isCancelled();
	
	/**
	 * Sets the {@code ReceiptType} record.
	 */
	void setReceiptType(ReceiptType type);
	
	/**
	 * Returns the {@code ReceiptType} record.
	 */
	ReceiptType getReceiptType();
	
	/**
	 * Sets the {@code Cashbox} record.
	 */
	void setCashbox(Cashbox one);
	
	/**
	 * Returns the {@code Cashbox} record.
	 */
	Cashbox getCashbox();
}
