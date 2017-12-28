package ua.kapitonenko.domain;

public class ReceiptProduct extends BaseEntity {
	
	private Long receiptId;
	private Long productId;
	private String quantity;
	
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
	
	public String getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
}