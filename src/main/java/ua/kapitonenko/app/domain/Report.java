package ua.kapitonenko.app.domain;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.records.Cashbox;
import ua.kapitonenko.app.domain.records.PaymentType;
import ua.kapitonenko.app.domain.records.TaxCategory;
import ua.kapitonenko.app.domain.records.ZReport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Report extends Model implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Report.class);
	
	private Long userId;
	private Cashbox cashbox;
	private ReportType type;
	private String docType;
	private Date createdAt = new Date();
	private BigDecimal deposit = new BigDecimal("0.00");
	private BigDecimal withdrawal = new BigDecimal("0.00");
	private ZReport record;
	
	private ReportSummary salesFigures;
	private ReportSummary refundsFigures;
	
	private List<ReportField> fields = new ArrayList<>();
	
	
	public Report(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	
	public void initSummary(List<Receipt> receipts,
	                        List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		
		Map<Long, List<Receipt>> map = receipts.stream().collect(Collectors.groupingBy(receipt -> receipt.getRecord().getReceiptTypeId()));
		
		List<Receipt> sales = map.getOrDefault(Application.getId(Application.RECEIPT_TYPE_FISCAL), Collections.emptyList());
		salesFigures = new ReportSummary(sales, taxCats, paymentTypes);
		
		List<Receipt> refunds = map.getOrDefault(Application.getId(Application.RECEIPT_TYPE_RETURN), Collections.emptyList());
		refundsFigures = new ReportSummary(refunds, taxCats, paymentTypes);
		initFields();
	}
	
	
	public List<ReportField> getFields() {
		return fields;
	}
	
	private void initFields() {
		fields.add(new ReportField(true, Keys.NO_RECEIPTS,
				                          BigDecimal.valueOf(salesFigures.getNoReceipts()),
				                          BigDecimal.valueOf(refundsFigures.getNoReceipts()),
				                          null, 0));
		fields.add(new ReportField(false, Keys.NO_ARTICLES,
				                          BigDecimal.valueOf(salesFigures.getNoArticles()),
				                          BigDecimal.valueOf(refundsFigures.getNoArticles()),
				                          null, 0));
		fields.add(new ReportField(false, Keys.NO_CANCELLED,
				                          BigDecimal.valueOf(salesFigures.getNoCancelled()),
				                          BigDecimal.valueOf(refundsFigures.getNoCancelled()),
				                          null, 0));
		
		for (PaymentType type : salesFigures.costPerPayType().keySet()) {
			fields.add(new ReportField(false, type.getBundleKey(),
					                          salesFigures.costPerPayType().get(type),
					                          refundsFigures.costPerPayType().get(type),
					                          type.getBundleName(),
					                          2
			));
		}
		
		fields.add(new ReportField(false, Keys.TURNOVER_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : salesFigures.costPerTaxCat().keySet()) {
			fields.add(new ReportField(false, cat.getBundleKey(),
					                          salesFigures.costPerTaxCat().get(cat),
					                          refundsFigures.costPerTaxCat().get(cat),
					                          cat.getBundleName(),
					                          2
			));
		}
		fields.add(new ReportField(true, Keys.TOTAL_TURNOVER,
				                          salesFigures.getTotalCost(),
				                          refundsFigures.getTotalCost(),
				                          null,
				                          2));
		
		fields.add(new ReportField(false, Keys.TAX_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : salesFigures.costPerTaxCat().keySet()) {
			fields.add(new ReportField(false, cat.getBundleKey(),
					                          salesFigures.taxPerTaxCat().get(cat),
					                          refundsFigures.taxPerTaxCat().get(cat),
					                          cat.getBundleName(),
					                          2
			));
		}
		fields.add(new ReportField(true, Keys.TAX_AMOUNT,
				                          salesFigures.getTaxAmount(),
				                          refundsFigures.getTaxAmount(),
				                          null, 2));
	}
	
	
	public Date getCreatedAt() {
		if (record != null) {
			return record.getCreatedAt();
		}
		return createdAt;
	}
	
	public ReportType getType() {
		return type;
	}
	
	public void setType(ReportType type) {
		this.type = type;
	}
	
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	public BigDecimal getDeposit() {
		return deposit;
	}
	
	public BigDecimal getWithdrawal() {
		return withdrawal;
	}
	
	public BigDecimal getCashBalance() {
		return salesFigures.getCashAmount().subtract(refundsFigures.getCashAmount());
	}
	
	public String getDocType() {
		if (type == ReportType.Z_REPORT) {
			return Keys.FISCAL;
		}
		
		return null;
	}
	
	public ZReport getRecord() {
		return record;
	}
	
	public void setRecord(ZReport record) {
		this.record = record;
	}
}
