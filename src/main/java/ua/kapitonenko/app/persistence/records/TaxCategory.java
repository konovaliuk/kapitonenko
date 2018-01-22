package ua.kapitonenko.app.persistence.records;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class TaxCategory extends BaseLocalizedEntity {
	
	private BigDecimal rate;
	
	public TaxCategory() {
	}
	
	public TaxCategory(Long id, String name, String bundleName, String bundleKey, BigDecimal rate) {
		super(id, name, bundleName, bundleKey);
		this.rate = rate;
	}
	
	public BigDecimal getRate() {
		return rate;
	}
	
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!super.equals(o)){
			return false;
		}
		
		TaxCategory that = (TaxCategory) o;
		
		return this.rate.equals(that.rate);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(super.hashCode())
				       .append(rate)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("TaxCategory")
				       .append(super.toString())
				       .append(", rate=").append(rate)
				       .append("}")
				       .toString();
	}

}