package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * {@code Product} interface defines methods for working with {@link ProductRecord} and all its dependencies.
 */
public interface Product extends Model {
	
	/**
	 * Returns id of {@code ProductRecord}.
	 */
	Long getId();
	
	/**
	 * Returns id of active {@code LocaleRecord}.
	 */
	Long getLocaleId();
	
	/**
	 * Sets the id of active {@code LocaleRecord}.
	 */
	void setLocaleId(Long localeId);
	
	/**
	 * Returns the name of Product, based on current locale.
	 */
	String getName();
	
	/**
	 * Returns {@code TaxCategory} record.
	 */
	TaxCategory getTaxCategory();
	
	/**
	 * Sets {@code TaxCategory} record.
	 */
	void setTaxCategory(TaxCategory taxCategory);
	
	/**
	 * Returns {@code Unit} record.
	 */
	Unit getUnit();
	
	/**
	 * Sets {@code Unit} record.
	 */
	void setUnit(Unit unit);
	
	/**
	 * Returns {@code Unit} record id.
	 */
	Long getUnitId();
	
	/**
	 * Sets {@code Unit} record id.
	 */
	void setUnitId(Long unitId);
	
	/**
	 * Returns the price of product.
	 */
	BigDecimal getPrice();
	
	/**
	 * Sets the price of product.
	 */
	void setPrice(BigDecimal price);
	
	/**
	 * Returns gross total of product items.
	 */
	BigDecimal getCost();
	
	/**
	 * Returns tax amount of product items.
	 */
	BigDecimal getTax();
	
	/**
	 * Returns {@code TaxCategory} record id.
	 */
	Long getTaxCategoryId();
	
	/**
	 * Sets {@code TaxCategory} record id.
	 */
	void setTaxCategoryId(Long taxCategoryId);
	
	/**
	 * Returns the number of products.
	 */
	BigDecimal getQuantity();
	
	/**
	 * Sets the number of products.
	 */
	void setQuantity(BigDecimal quantity);
	
	/**
	 * Sets {@link User} record id.
	 */
	void setCreatedBy(Long createdBy);
	
	/**
	 * Returns the list of {@code ProductLocale} records, associated with the product.
	 */
	List<ProductLocale> getNames();
	
	/**
	 * Sets the list of {@code ProductLocale} records, associated with the product.
	 */
	void setNames(List<ProductLocale> names);
	
	/**
	 * Adds the number of product items.
	 */
	void addQuantity(BigDecimal q);
	
	/**
	 * Sets the name for each {@link ProductLocale}, associated with the product.
	 */
	void fillNames(String[] lang);
	
	/**
	 * Returns the {@code ProductRecord} instance.
	 */
	ProductRecord getRecord();
	
	/**
	 * Sets the {@code ProductRecord} instance.
	 */
	void setRecord(ProductRecord created);
	
	/**
	 * Instantiate the list of {@code ProductLocale} for each {@code LocaleRecord}.
	 */
	void initNames(List<LocaleRecord> locales);
}
