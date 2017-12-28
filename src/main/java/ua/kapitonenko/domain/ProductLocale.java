package ua.kapitonenko.domain;

public class ProductLocale extends BaseEntity {
	
	private Long productId;
	private Long locale;
	private String propertyName;
	private String propertyValue;
	
	public Long getProductId() {
		return this.productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Long getLocale() {
		return this.locale;
	}
	
	public void setLocale(Long locale) {
		this.locale = locale;
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
	
}