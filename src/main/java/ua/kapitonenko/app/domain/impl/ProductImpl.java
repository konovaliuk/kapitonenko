package ua.kapitonenko.app.domain.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductImpl implements Product {
	
	private ProductRecord record;
	private String name;
	private Long localeId = Application.Ids.DEFAULT_LOCALE.getValue();
	private List<ProductLocale> names = new ArrayList<>();
	private TaxCategory taxCategory;
	private Unit unit;
	
	ProductImpl(ProductRecord record) {
		this.record = record;
	}
	
	@Override
	public void initNames(List<LocaleRecord> locales) {
		for (LocaleRecord locale : locales) {
			names.add(new ProductLocale(record, locale, Keys.PRODUCT_NAME, null));
		}
	}
	
	@Override
	public String getName() {
		if (this.name == null && names != null && !names.isEmpty()) {
			name = names.stream()
					       .filter(pl -> pl.getLocaleId().equals(localeId) && pl.getPropertyName().equals(Keys.PRODUCT_NAME))
					       .map(ProductLocale::getPropertyValue).collect(Collectors.joining());
		}
		return name;
	}
	
	@Override
	public void fillNames(String[] lang) {
		for (int i = 0; i < names.size(); i++) {
			ProductLocale productLocale = names.get(i);
			productLocale.setPropertyValue(lang[i].trim());
		}
	}
	
	@Override
	public BigDecimal getCost() {
		return record.getPrice().multiply(record.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	@Override
	public BigDecimal getTax() {
		return getCost().movePointLeft(2)
				       .multiply(taxCategory.getRate())
				       .setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	@Override
	public void addQuantity(BigDecimal q) {
		record.setQuantity(getQuantity().add(q));
	}
	
	@Override
	public Long getTaxCategoryId() {
		return null;
	}
	
	@Override
	public void setTaxCategoryId(Long taxCategoryId) {
		record.setTaxCategoryId(taxCategoryId);
	}
	
	@Override
	public BigDecimal getQuantity() {
		return record.getQuantity();
	}
	
	@Override
	public void setQuantity(BigDecimal quantity) {
		record.setQuantity(quantity);
	}
	
	@Override
	public void setCreatedBy(Long createdBy) {
		record.setCreatedBy(createdBy);
	}
	
	@Override
	public List<ProductLocale> getNames() {
		return names;
	}
	
	@Override
	public void setNames(List<ProductLocale> names) {
		this.names = names;
	}
	
	@Override
	public ProductRecord getRecord() {
		return record;
	}
	
	@Override
	public void setRecord(ProductRecord created) {
	
	}
	
	@Override
	public TaxCategory getTaxCategory() {
		return taxCategory;
	}
	
	@Override
	public void setTaxCategory(TaxCategory taxCategory) {
		this.taxCategory = taxCategory;
	}
	
	@Override
	public Unit getUnit() {
		return unit;
	}
	
	@Override
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public Long getUnitId() {
		return record.getUnitId();
	}
	
	@Override
	public void setUnitId(Long unitId) {
		record.setUnitId(unitId);
	}
	
	@Override
	public Long getId() {
		return record.getId();
		
	}
	
	@Override
	public Long getLocaleId() {
		return localeId;
	}
	
	@Override
	public void setLocaleId(Long localeId) {
		this.localeId = localeId;
	}
	
	@Override
	public BigDecimal getPrice() {
		return record.getPrice();
	}
	
	@Override
	public void setPrice(BigDecimal price) {
		record.setPrice(price);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ProductImpl product = (ProductImpl) o;
		
		return new EqualsBuilder()
				       .append(record, product.record)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(record)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		if (record != null) {
			return record.toString();
		}
		return super.toString();
	}
}
