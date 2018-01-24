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

/**
 * Implementation of {@code Product} interface.
 */
public class ProductImpl implements Product {
	
	private ProductRecord record;
	private String name;
	private Long localeId = Application.Ids.DEFAULT_LOCALE.getValue();
	private List<ProductLocale> names = new ArrayList<>();
	private TaxCategory taxCategory;
	private Unit unit;
	
	/**
	 * Constructor instantiate the {@code Product} with the given {@code ProductRecord}.
	 * Is used only by {@link ua.kapitonenko.app.domain.ModelFactory}.
	 */
	ProductImpl(ProductRecord record) {
		this.record = record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initNames(List<LocaleRecord> locales) {
		for (LocaleRecord locale : locales) {
			names.add(new ProductLocale(record, locale, Keys.PRODUCT_NAME, null));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		if (this.name == null && names != null && !names.isEmpty()) {
			name = names.stream()
					       .filter(pl -> pl.getLocaleId().equals(localeId) && pl.getPropertyName().equals(Keys.PRODUCT_NAME))
					       .map(ProductLocale::getPropertyValue).collect(Collectors.joining());
		}
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillNames(String[] lang) {
		for (int i = 0; i < names.size(); i++) {
			ProductLocale productLocale = names.get(i);
			productLocale.setPropertyValue(lang[i].trim());
		}
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getCost() {
		return record.getPrice().multiply(record.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTax() {
		return getCost().movePointLeft(2)
				       .multiply(taxCategory.getRate())
				       .setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addQuantity(BigDecimal q) {
		record.setQuantity(getQuantity().add(q));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getTaxCategoryId() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaxCategoryId(Long taxCategoryId) {
		record.setTaxCategoryId(taxCategoryId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getQuantity() {
		return record.getQuantity();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setQuantity(BigDecimal quantity) {
		record.setQuantity(quantity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreatedBy(Long createdBy) {
		record.setCreatedBy(createdBy);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductLocale> getNames() {
		return names;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNames(List<ProductLocale> names) {
		this.names = names;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductRecord getRecord() {
		return record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRecord(ProductRecord created) {
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TaxCategory getTaxCategory() {
		return taxCategory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTaxCategory(TaxCategory taxCategory) {
		this.taxCategory = taxCategory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Unit getUnit() {
		return unit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getUnitId() {
		return record.getUnitId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUnitId(Long unitId) {
		record.setUnitId(unitId);
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
	public Long getLocaleId() {
		return localeId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocaleId(Long localeId) {
		this.localeId = localeId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getPrice() {
		return record.getPrice();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrice(BigDecimal price) {
		record.setPrice(price);
	}
	
	/**
	 * Products are equal if ProductRecords are equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (record == null) {
			return super.equals(o);
		}
		
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ProductImpl product = (ProductImpl) o;
		
		return new EqualsBuilder()
				       .append(record, product.record)
				       .isEquals();
	}
	
	/**
	 * Calculates hashCode based on ProductRecord.
	 */
	@Override
	public int hashCode() {
		if (record == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(17, 37)
				       .append(record)
				       .toHashCode();
	}
	
	/**
	 * Returns result of {@code ProductRecord} toString() method.
	 */
	@Override
	public String toString() {
		if (record != null) {
			return record.toString();
		}
		return super.toString();
	}
}
