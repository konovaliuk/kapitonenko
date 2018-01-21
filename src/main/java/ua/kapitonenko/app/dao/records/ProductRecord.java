package ua.kapitonenko.app.dao.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.Date;

public class ProductRecord extends BaseEntity {
	
	private Long unitId;
	private BigDecimal price;
	private Long taxCategoryId;
	private BigDecimal quantity;
	private Date createdAt;
	private Long createdBy;
	private Date deletedAt;
	private Long deletedBy;
	
	public ProductRecord() {
	}
	
	public ProductRecord(Long unitId, BigDecimal price, Long taxCategoryId, BigDecimal quantity, Long createdBy) {
		this.unitId = unitId;
		this.price = price;
		this.taxCategoryId = taxCategoryId;
		this.quantity = quantity;
		this.createdBy = createdBy;
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
	
	public Date getCreatedAt() {
		return this.createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getDeletedAt() {
		return this.deletedAt;
	}
	
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public Long getDeletedBy() {
		return this.deletedBy;
	}
	
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ProductRecord product = (ProductRecord) o;
		
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