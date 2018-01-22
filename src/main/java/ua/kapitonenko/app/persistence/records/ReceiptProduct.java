package ua.kapitonenko.app.persistence.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class ReceiptProduct extends BaseEntity {
	
	private Long receiptId;
	private Long productId;
	private BigDecimal quantity;
	
	public ReceiptProduct() {
	}
	
	public ReceiptProduct(Long id, Long receiptId, Long productId, BigDecimal quantity) {
		super(id);
		this.receiptId = receiptId;
		this.productId = productId;
		this.quantity = quantity;
	}
	
	public Long getReceiptId() {
		return this.receiptId;
	}
	
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	
	public Long getProductId() {
		return this.productId;
	}
	
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ReceiptProduct that = (ReceiptProduct) o;
		
		return new EqualsBuilder()
				       .append(getId(), that.getId())
				       .append(receiptId, that.receiptId)
				       .append(productId, that.productId)
				       .append(quantity, that.quantity)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(receiptId)
				       .append(productId)
				       .append(quantity)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ReceiptProduct{")
				       .append("id=").append(getId())
				       .append(", receiptId=").append(receiptId)
				       .append(", productId=").append(productId)
				       .append(", quantity=").append(quantity)
				       .append("}")
				       .toString();
	}
}