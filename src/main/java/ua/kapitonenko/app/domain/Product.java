package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.List;

public interface Product extends Model {
	
	Long getId();
	
	Long getLocaleId();
	
	void setLocaleId(Long localeId);
	
	String getName();
	
	TaxCategory getTaxCategory();
	
	void setTaxCategory(TaxCategory taxCategory);
	
	Unit getUnit();
	
	void setUnit(Unit unit);
	
	Long getUnitId();
	
	void setUnitId(Long unitId);
	
	BigDecimal getPrice();
	
	void setPrice(BigDecimal price);
	
	BigDecimal getCost();
	
	BigDecimal getTax();
	
	Long getTaxCategoryId();
	
	void setTaxCategoryId(Long taxCategoryId);
	
	BigDecimal getQuantity();
	
	void setQuantity(BigDecimal quantity);
	
	void setCreatedBy(Long createdBy);
	
	List<ProductLocale> getNames();
	
	void setNames(List<ProductLocale> names);
	
	void addQuantity(BigDecimal q);
	
	void fillNames(String[] lang);
	
	ProductRecord getRecord();
	
	void setRecord(ProductRecord created);
	
	void initNames(List<LocaleRecord> locales);
}
