package ua.kapitonenko.domain;

public class Receipt extends BaseEntity {
	
	private Long machineId;
	private Long paymentTypeId;
	private Long receiptTypeId;
	private Long cancelled;
	private java.util.Date createdAt;
	private Long createdBy;
	
	public Long getMachineId() {
		return this.machineId;
	}
	
	public void setMachineId(Long machineId) {
		this.machineId = machineId;
	}
	
	public Long getPaymentTypeId() {
		return this.paymentTypeId;
	}
	
	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	
	public Long getReceiptTypeId() {
		return this.receiptTypeId;
	}
	
	public void setReceiptTypeId(Long receiptTypeId) {
		this.receiptTypeId = receiptTypeId;
	}
	
	public Long getCancelled() {
		return this.cancelled;
	}
	
	public void setCancelled(Long cancelled) {
		this.cancelled = cancelled;
	}
	
	public java.util.Date getCreatedAt() {
		return this.createdAt;
	}
	
	public void setCreatedAt(java.util.Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
}