package ua.kapitonenko.domain;

import java.sql.Timestamp;

public class Product extends BaseEntity {
	
	private Long unitId;
	private String price;
	private Long taxCategoryId;
	private String quantity;
	private Timestamp createdAt;
	private Long createdBy;
	private Timestamp deletedAt;
	private Long deletedBy;
	
	public Long getUnitId() {
		return this.unitId;
	}
	
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public String getPrice() {
		return this.price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public Long getTaxCategoryId() {
		return this.taxCategoryId;
	}
	
	public void setTaxCategoryId(Long taxCategoryId) {
		this.taxCategoryId = taxCategoryId;
	}
	
	public String getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(String quantity) {
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
	
}