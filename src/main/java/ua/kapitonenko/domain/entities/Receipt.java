package ua.kapitonenko.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;
import java.util.List;

public class Receipt extends BaseEntity {
	
	private Long cashboxId;
	private Long paymentTypeId;
	private Long receiptTypeId;
	private boolean cancelled;
	private Timestamp createdAt;
	private Long createdBy;
	
	private Cashbox cashbox;
	private PaymentType paymentType;
	private ReceiptType receiptType;
	private User userCreateBy;
	private List<ReceiptProduct> products;
	
	public Receipt() {
	}
	
	public Receipt(Long id, Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		super(id);
		this.cashboxId = cashboxId;
		this.paymentTypeId = paymentTypeId;
		this.receiptTypeId = receiptTypeId;
		this.cancelled = cancelled;
		this.createdBy = createdBy;
	}
	
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	public ReceiptType getReceiptType() {
		return receiptType;
	}
	
	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}
	
	public User getUserCreateBy() {
		return userCreateBy;
	}
	
	public void setUserCreateBy(User userCreateBy) {
		this.userCreateBy = userCreateBy;
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
		
		Receipt receipt = (Receipt) o;
		
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
	
	public List<ReceiptProduct> getProducts() {
		return products;
	}
	
	public void setProducts(List<ReceiptProduct> products) {
		this.products = products;
	}
}