package ua.kapitonenko.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.keys.Keys;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class Product extends BaseEntity {
	private static final Logger LOGGER = Logger.getLogger(Product.class);
	
	private Long unitId;
	private BigDecimal price;
	private Long taxCategoryId;
	private BigDecimal quantity;
	private Timestamp createdAt;
	private Long createdBy;
	private Timestamp deletedAt;
	private Long deletedBy;
	
	private TaxCategory taxCategory;
	private Unit unit;
	private User userCreatedBy;
	private User userDeletedBy;
	private List<ProductLocale> names;
	private String name;
	private BigDecimal cost;
	
	
	private Long localeId = Application.getId(Application.DEFAULT_LOCALE);
	
	public Product() {
	}
	
	public Product(Long unitId, BigDecimal price, Long taxCategoryId, BigDecimal quantity, Long createdBy) {
		this.unitId = unitId;
		this.price = price;
		this.taxCategoryId = taxCategoryId;
		this.quantity = quantity;
		this.createdBy = createdBy;
	}
	
	public Long getLocaleId() {
		return localeId;
	}
	
	public void setLocaleId(Long localeId) {
		this.localeId = localeId;
	}
	
	public String getName() {
		if (this.name == null && names != null && !names.isEmpty()) {
			
			return names.stream()
					       .filter(pl -> pl.getLocaleId().equals(localeId) && pl.getPropertyName().equals(Keys.PRODUCT_NAME))
					       .map(ProductLocale::getPropertyValue).collect(Collectors.joining());
		}
		
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public TaxCategory getTaxCategory() {
		return taxCategory;
	}
	
	public void setTaxCategory(TaxCategory taxCategory) {
		this.taxCategory = taxCategory;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public User getUserCreatedBy() {
		return userCreatedBy;
	}
	
	public void setUserCreatedBy(User userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}
	
	public User getUserDeletedBy() {
		return userDeletedBy;
	}
	
	public void setUserDeletedBy(User userDeletedBy) {
		this.userDeletedBy = userDeletedBy;
	}
	
	public Long getUnitId() {
		return this.unitId;
	}
	
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getCost() {
		
		this.cost = price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		return this.cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public BigDecimal getTax() {
		return getCost().movePointLeft(2)
				       .multiply(taxCategory.getRate())
				       .setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	
	public Long getTaxCategoryId() {
		return this.taxCategoryId;
	}
	
	public void setTaxCategoryId(Long taxCategoryId) {
		this.taxCategoryId = taxCategoryId;
	}
	
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public Timestamp getCreatedAt() {
		return this.createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	public Timestamp getDeletedAt() {
		return this.deletedAt;
	}
	
	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public Long getDeletedBy() {
		return this.deletedBy;
	}
	
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}
	
	public List<ProductLocale> getNames() {
		return names;
	}
	
	public void setNames(List<ProductLocale> names) {
		this.names = names;
	}
	
	public void addQuantity(BigDecimal q) {
		quantity = quantity.add(q);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		Product product = (Product) o;
		
		return new EqualsBuilder()
				       .append(getId(), product.getId())
				       .append(unitId, product.unitId)
				       .append(price, product.price)
				       .append(taxCategoryId, product.taxCategoryId)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(unitId)
				       .append(price)
				       .append(taxCategoryId)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Product{")
				       .append("id=").append(getId())
				       .append(", unitId=").append(unitId)
				       .append(", price=").append(price)
				       .append(", taxCategoryId=").append(taxCategoryId)
				       .append(", quantity=").append(quantity)
				       .append(", createdAt=").append(createdAt)
				       .append(", createdBy=").append(createdBy)
				       .append(", deletedAt=").append(deletedAt)
				       .append(", deletedBy=").append(deletedBy)
				       .append("}")
				       .toString();
	}
	
	
}