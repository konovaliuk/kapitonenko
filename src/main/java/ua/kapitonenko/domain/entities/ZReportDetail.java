package ua.kapitonenko.domain.entities;

public class ZReportDetail extends BaseEntity {
	
	private Long zReportId;
	private Long receiptTypeId;
	private Long receiptsNumber;
	private Long cancelledNumber;
	private Long productsNumber;
	private String cashAmount;
	private String cat1Amount;
	private String cat2Amount;
	private String totalAmount;
	private String tax1Amount;
	private String tax2Amount;
	private String taxTaxAmount;
	
	public Long getZReportId() {
		return this.zReportId;
	}
	
	public void setZReportId(Long zReportId) {
		this.zReportId = zReportId;
	}
	
	public Long getReceiptTypeId() {
		return this.receiptTypeId;
	}
	
	public void setReceiptTypeId(Long receiptTypeId) {
		this.receiptTypeId = receiptTypeId;
	}
	
	public Long getReceiptsNumber() {
		return this.receiptsNumber;
	}
	
	public void setReceiptsNumber(Long receiptsNumber) {
		this.receiptsNumber = receiptsNumber;
	}
	
	public Long getCancelledNumber() {
		return this.cancelledNumber;
	}
	
	public void setCancelledNumber(Long cancelledNumber) {
		this.cancelledNumber = cancelledNumber;
	}
	
	public Long getProductsNumber() {
		return this.productsNumber;
	}
	
	public void setProductsNumber(Long productsNumber) {
		this.productsNumber = productsNumber;
	}
	
	public String getCashAmount() {
		return this.cashAmount;
	}
	
	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public String getCat1Amount() {
		return this.cat1Amount;
	}
	
	public void setCat1Amount(String cat1Amount) {
		this.cat1Amount = cat1Amount;
	}
	
	public String getCat2Amount() {
		return this.cat2Amount;
	}
	
	public void setCat2Amount(String cat2Amount) {
		this.cat2Amount = cat2Amount;
	}
	
	public String getTotalAmount() {
		return this.totalAmount;
	}
	
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getTax1Amount() {
		return this.tax1Amount;
	}
	
	public void setTax1Amount(String tax1Amount) {
		this.tax1Amount = tax1Amount;
	}
	
	public String getTax2Amount() {
		return this.tax2Amount;
	}
	
	public void setTax2Amount(String tax2Amount) {
		this.tax2Amount = tax2Amount;
	}
	
	public String getTaxTaxAmount() {
		return this.taxTaxAmount;
	}
	
	public void setTaxTaxAmount(String taxTaxAmount) {
		this.taxTaxAmount = taxTaxAmount;
	}
	
}