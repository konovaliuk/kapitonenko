package ua.kapitonenko.app.fixtures;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ua.kapitonenko.app.dao.records.*;
import ua.kapitonenko.app.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class ProductMock implements Product {
	private ProductRecord record;
	private Long localeId;
	private BigDecimal quantity;
	private TaxCategory taxCategory;
	private BigDecimal taxAmount;
	private BigDecimal cost;
	private Long id;
	private List<ProductLocale> names;
	
	private boolean setNamesCalled;
	private boolean setTaxCatCalled;
	private boolean setUnitCalled;
	
	@Override
	public Long getId() {
		if (id == null) {
			id = new Random().nextLong();
		}
		return id;
	}
	
	@Override
	public ProductRecord getRecord() {
		if (record != null) {
			return record;
		}
		record = new ProductRecord();
		record.setId(new Random().nextLong());
		return record;
	}
	
	@Override
	public void setRecord(ProductRecord record) {
		this.record = record;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ProductMock product = (ProductMock) o;
		
		return new EqualsBuilder()
				       .append(getId(), product.getId())
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .toHashCode();
	}
	
	@Override
	public BigDecimal getQuantity() {
		if (quantity == null) {
			return BigDecimal.valueOf(new Random().nextLong());
		}
		return quantity;
	}
	
	@Override
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public void addQuantity(BigDecimal q) {
		quantity = getQuantity().add(q);
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
	public TaxCategory getTaxCategory() {
		return taxCategory;
	}
	
	@Override
	public void setTaxCategory(TaxCategory taxCategory) {
		setTaxCatCalled = true;
		this.taxCategory = taxCategory;
	}
	
	@Override
	public BigDecimal getTax() {
		return taxAmount;
	}
	
	public void setTax(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	@Override
	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public Unit getUnit() {
		return null;
	}
	
	@Override
	public void setUnit(Unit unit) {
		setUnitCalled = true;
	}
	
	@Override
	public Long getUnitId() {
		return null;
	}
	
	@Override
	public void setUnitId(Long unitId) {
	
	}
	
	@Override
	public BigDecimal getPrice() {
		return null;
	}
	
	@Override
	public void setPrice(BigDecimal price) {
	
	}
	
	@Override
	public Long getTaxCategoryId() {
		return null;
	}
	
	@Override
	public void setTaxCategoryId(Long taxCategoryId) {
	
	}
	
	@Override
	public void setCreatedBy(Long createdBy) {
	
	}
	
	@Override
	public List<ProductLocale> getNames() {
		return names;
	}
	
	@Override
	public void setNames(List<ProductLocale> names) {
		setNamesCalled = true;
		this.names = names;
	}
	
	@Override
	public void fillNames(String[] lang) {
	
	}
	
	@Override
	public void initNames(List<LocaleRecord> locales) {
		names = new ArrayList<>();
		for (LocaleRecord locale : locales) {
			names.add(new ProductLocale(record, locale, null, null));
		}
	}
	
	public void verifyDependencies() {
		assertTrue(setTaxCatCalled);
		assertTrue(setNamesCalled);
		assertTrue(setUnitCalled);
	}
}
