package ua.kapitonenko.app.dao.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class ReceiptRecord extends BaseEntity {
	
	private Long cashboxId;
	private Long paymentTypeId;
	private Long receiptTypeId;
	private boolean cancelled;
	private Date createdAt;
	private Long createdBy;
	
	public ReceiptRecord() {
	}
	
	public ReceiptRecord(Long id, Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		super(id);
		this.cashboxId = cashboxId;
		this.paymentTypeId = paymentTypeId;
		this.receiptTypeId = receiptTypeId;
		this.cancelled = cancelled;
		this.createdBy = createdBy;
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
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Long getCashboxId() {
		return cashboxId;
	}
	
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ReceiptRecord receipt = (ReceiptRecord) o;
		
		return new EqualsBuilder()
				       .append(getId(), receipt.getId())
				       .append(cancelled, receipt.cancelled)
				       .append(cashboxId, receipt.cashboxId)
				       .append(paymentTypeId, receipt.paymentTypeId)
				       .append(receiptTypeId, receipt.receiptTypeId)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(cashboxId)
				       .append(paymentTypeId)
				       .append(receiptTypeId)
				       .append(cancelled)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Receipt{")
				       .append("id=").append(getId())
				       .append(", cashboxId=").append(cashboxId)
				       .append(", paymentTypeId=").append(paymentTypeId)
				       .append(", receiptTypeId=").append(receiptTypeId)
				       .append(", cancelled=").append(cancelled)
				       .append(", createdAt=").append(createdAt)
				       .append(", createdBy=").append(createdBy)
				       .append("}")
				       .toString();
	}
}