package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.dao.records.Cashbox;
import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.dao.records.ZReport;
import ua.kapitonenko.app.domain.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReportStub implements Report {
	private ZReport record;
	private Cashbox cashbox;
	private boolean summaryInitialized;
	private ReportType reportType;
	
	@Override
	public Long getUserId() {
		return new Random().nextLong();
	}
	
	@Override
	public Long getCashboxId() {
		return new Random().nextLong();
	}
	
	@Override
	public BigDecimal getCashBalance() {
		return BigDecimal.valueOf(new Random().nextLong());
	}
	
	@Override
	public ZReport getRecord() {
		return record;
	}
	
	@Override
	public void setRecord(ZReport record) {
		this.record = record;
	}
	
	@Override
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	@Override
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	@Override
	public void initSummary(List<Receipt> receipts, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		summaryInitialized = true;
	}
	
	public boolean isSummaryInitialized() {
		return summaryInitialized;
	}
	
	@Override
	public ReportType getType() {
		return reportType;
	}
	
	@Override
	public void setType(ReportType type) {
		reportType = type;
	}
	
	@Override
	public ReportSummary getSalesFigures() {
		return null;
	}
	
	@Override
	public ReportSummary getRefundsFigures() {
		return null;
	}
	
	
	@Override
	public List<ReportField> getFields() {
		return null;
	}
	
	@Override
	public Date getCreatedAt() {
		return null;
	}
	
	
	@Override
	public BigDecimal getDeposit() {
		return null;
	}
	
	@Override
	public BigDecimal getWithdrawal() {
		return null;
	}
	
	@Override
	public String getDocType() {
		return null;
	}
	
	@Override
	public void setCashboxId(Long cashboxId) {
	
	}
	
	
}
