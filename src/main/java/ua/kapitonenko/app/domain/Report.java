package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.dao.records.Cashbox;
import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.dao.records.ZReport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface Report extends Model {
	Long getUserId();
	
	ReportSummary getSalesFigures();
	
	ReportSummary getRefundsFigures();
	
	void initSummary(List<Receipt> receipts,
	                 List<TaxCategory> taxCats, List<PaymentType> paymentTypes);
	
	List<ReportField> getFields();
	
	Date getCreatedAt();
	
	ReportType getType();
	
	void setType(ReportType type);
	
	Cashbox getCashbox();
	
	void setCashbox(Cashbox cashbox);
	
	BigDecimal getDeposit();
	
	BigDecimal getWithdrawal();
	
	BigDecimal getCashBalance();
	
	String getDocType();
	
	ZReport getRecord();
	
	void setRecord(ZReport record);
	
	void setCashboxId(Long cashboxId);
	
	Long getCashboxId();
}
