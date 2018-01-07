package ua.kapitonenko.domain;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.domain.entities.Cashbox;
import ua.kapitonenko.domain.entities.PaymentType;
import ua.kapitonenko.domain.entities.TaxCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report extends Model implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Report.class);
	
	private Cashbox cashbox;
	private ReportType type;
	private String docType;
	private Date createdAt;
	private BigDecimal deposit = new BigDecimal("0.00");
	private BigDecimal withdrawal = new BigDecimal("0.00");
	
	private ReportSummary salesFigures;
	private ReportSummary refundsFigures;
	
	private List<ReportField> fields = new ArrayList<>();
	
	private boolean initialized;
	
	public Report() {
		createdAt = new Date();
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void initSummary(List<ReceiptCalculator> sales, List<ReceiptCalculator> refunds,
	                        List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		salesFigures = new ReportSummary(sales, taxCats, paymentTypes);
		refundsFigures = new ReportSummary(refunds, taxCats, paymentTypes);
		initFields();
		initialized = true;
	}
	
	
	public List<ReportField> getFields() {
		return fields;
	}
	
	private void initFields() {
		fields.add(new ReportField(Keys.NO_RECEIPTS,
				                          BigDecimal.valueOf(salesFigures.getNoReceipts()),
				                          BigDecimal.valueOf(refundsFigures.getNoReceipts()),
				                          null, 0));
		fields.add(new ReportField(Keys.NO_ARTICLES,
				                          BigDecimal.valueOf(salesFigures.getNoArticles()),
				                          BigDecimal.valueOf(refundsFigures.getNoArticles()),
				                          null, 0));
		fields.add(new ReportField(Keys.NO_CANCELLED,
				                          BigDecimal.valueOf(salesFigures.getNoCancelled()),
				                          BigDecimal.valueOf(refundsFigures.getNoCancelled()),
				                          null, 0));
		
		for (PaymentType type : salesFigures.costPerPayType().keySet()) {
			fields.add(new ReportField(type.getBundleKey(),
					                          salesFigures.costPerPayType().get(type),
					                          refundsFigures.costPerPayType().get(type),
					                          type.getBundleName(),
					                          2
			));
		}
		
		fields.add(new ReportField(Keys.TURNOVER_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : salesFigures.costPerTaxCat().keySet()) {
			fields.add(new ReportField(cat.getBundleKey(),
					                          salesFigures.costPerTaxCat().get(cat),
					                          refundsFigures.costPerTaxCat().get(cat),
					                          cat.getBundleName(),
					                          2
			));
		}
		fields.add(new ReportField(Keys.TOTAL_TURNOVER,
				                          salesFigures.getTotalCost(),
				                          refundsFigures.getTotalCost(),
				                          null,
				                          2));
		
		fields.add(new ReportField(Keys.TAX_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : salesFigures.costPerTaxCat().keySet()) {
			fields.add(new ReportField(cat.getBundleKey(),
					                          salesFigures.taxPerTaxCat().get(cat),
					                          refundsFigures.taxPerTaxCat().get(cat),
					                          cat.getBundleName(),
					                          2
			));
		}
		fields.add(new ReportField(Keys.TAX_AMOUNT,
				                          salesFigures.getTaxAmount(),
				                          refundsFigures.getTaxAmount(),
				                          null, 2));
	}
	
	
	public Date getCreatedAt() {
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
		return salesFigures.getTotalCost().subtract(refundsFigures.getTotalCost());
	}
	
	public String getDocType() {
		if (type == ReportType.Z_REPORT) {
			return Keys.FISCAL;
		}
		
		return null;
	}
	
}
