package ua.kapitonenko.app.persistence.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ProductLocale extends BaseEntity {
	
	private Long productId;
	private Long localeId;
	private String propertyName;
	private String propertyValue;
	
	private LocaleRecord locale;
	private ProductRecord product;
	
	public ProductLocale() {
	}
	
	public ProductLocale(ProductRecord product, LocaleRecord locale, String propertyName, String propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		setLocale(locale);
		setProduct(product);
	}
	
	public ProductLocale(Long productId, Long localeId, String propertyName, String propertyValue) {
		this.productId = productId;
		this.localeId = localeId;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}
	
	public Long getProductId() {
		return this.productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Long getLocaleId() {
		return this.localeId;
	}
	
	public void setLocaleId(Long localeId) {
		this.localeId = localeId;
	}
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getPropertyValue() {
		return this.propertyValue;
	}
	
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	public LocaleRecord getLocale() {
		return locale;
	}
	
	public void setLocale(LocaleRecord locale) {
		this.locale = locale;
		if (locale != null && locale.getId() != null){
			localeId = locale.getId();
		}
	}
	
	public ProductRecord getProduct() {
		return product;
	}
	
	public void setProduct(ProductRecord product) {
		this.product = product;
		if (product != null && product.getId() != null){
			productId = product.getId();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ProductLocale that = (ProductLocale) o;
		
		return new EqualsBuilder()
				       .append(getId(), that.getId())
				       .append(productId, that.productId)
				       .append(localeId, that.localeId)
				       .append(propertyName, that.propertyName)
				       .append(propertyValue, that.propertyValue)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(productId)
				       .append(localeId)
				       .append(propertyName)
				       .append(propertyValue)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ProductLocale{")
				       .append("id=").append(getId())
				       .append(", productId=").append(productId)
				       .append(", localeId=").append(localeId)
				       .append(", propertyName=").append(propertyName)
				       .append(", propertyValue=").append(propertyValue)
				       .append("}")
				       .toString();
	}
}