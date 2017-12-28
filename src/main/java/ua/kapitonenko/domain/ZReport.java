package ua.kapitonenko.domain;

public class ZReport extends BaseEntity {
	
	private Long machineId;
	private Long lastReceiptId;
	private String cashBalance;
	private java.util.Date createdAt;
	private Long createdBy;
	
	public Long getMachineId() {
		return this.machineId;
	}
	
	public void setMachineId(Long machineId) {
		this.machineId = machineId;
	}
	
	public Long getLastReceiptId() {
		return this.lastReceiptId;
	}
	
	public void setLastReceiptId(Long lastReceiptId) {
		this.lastReceiptId = lastReceiptId;
	}
	
	public String getCashBalance() {
		return this.cashBalance;
	}
	
	public void setCashBalance(String cashBalance) {
		this.cashBalance = cashBalance;
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