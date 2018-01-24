package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.domain.*;
import ua.kapitonenko.app.persistence.records.Cashbox;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.TaxCategory;
import ua.kapitonenko.app.persistence.records.ZReport;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@code Report} interface.
 */
public class ReportImpl implements Report {
	
	private Long userId;
	private Long cashboxId;
	private Cashbox cashbox;
	private ReportType type;
	private Date createdAt = new Date();
	private ZReport record;
	
	private List<TaxCategory> taxCategoryList;
	private List<PaymentType> paymentTypeList;
	
	private ReportSummary salesFigures;
	private ReportSummary refundsFigures;
	
	private List<ReportField> fields = new ArrayList<>();
	
	private ModelFactory modelFactory;
	
	private BigDecimal deposit = new BigDecimal("0.00");
	private BigDecimal withdrawal = new BigDecimal("0.00");
	
	/**
	 * Constructor instantiate the {@code Report} with the given {@code User} record id.
	 * Is used only by {@link ua.kapitonenko.app.domain.ModelFactory}.
	 */
	ReportImpl(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setType(ReportType type) {
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}
	
	/**
	 * {@inheritDoc}
	 * Creates {@code ReportSummary} on sales and refunds operations.
	 * Initializes {@link ReportField} list.
	 */
	@Override
	public void initSummary(List<Receipt> receipts,
	                        List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		taxCategoryList = taxCats;
		paymentTypeList = paymentTypes;
		
		Map<Long, List<Receipt>> map = receipts.stream().collect(Collectors.groupingBy(Receipt::getTypeId));
		List<Receipt> sales = map.getOrDefault(Application.Ids.RECEIPT_TYPE_FISCAL.getValue(), Collections.emptyList());
		salesFigures = getModelFactory().createReportSummary(sales, taxCats, paymentTypes);
		
		List<Receipt> refunds = map.getOrDefault(Application.Ids.RECEIPT_TYPE_RETURN.getValue(), Collections.emptyList());
		refundsFigures = getModelFactory().createReportSummary(refunds, taxCats, paymentTypes);
		initFields();
	}
	
	private void initFields() {
		fields.add(modelFactory.createReportField(true, Keys.NO_RECEIPTS,
				BigDecimal.valueOf(salesFigures.getNoReceipts()),
				BigDecimal.valueOf(refundsFigures.getNoReceipts()),
				null, 0));
		fields.add(modelFactory.createReportField(false, Keys.NO_ARTICLES,
				BigDecimal.valueOf(salesFigures.getNoArticles()),
				BigDecimal.valueOf(refundsFigures.getNoArticles()),
				null, 0));
		fields.add(modelFactory.createReportField(false, Keys.NO_CANCELLED,
				BigDecimal.valueOf(salesFigures.getNoCancelled()),
				BigDecimal.valueOf(refundsFigures.getNoCancelled()),
				null, 0));
		
		for (PaymentType type : paymentTypeList) {
			fields.add(modelFactory.createReportField(false, type.getBundleKey(),
					salesFigures.costPerPayType().get(type),
					refundsFigures.costPerPayType().get(type),
					type.getBundleName(),
					2
			));
		}
		
		fields.add(modelFactory.createReportField(false, Keys.TURNOVER_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : taxCategoryList) {
			fields.add(modelFactory.createReportField(false, cat.getBundleKey(),
					salesFigures.costPerTaxCat().get(cat),
					refundsFigures.costPerTaxCat().get(cat),
					cat.getBundleName(),
					2
			));
		}
		fields.add(modelFactory.createReportField(true, Keys.TOTAL_TURNOVER,
				salesFigures.getTotalCost(),
				refundsFigures.getTotalCost(),
				null,
				2));
		
		fields.add(modelFactory.createReportField(false, Keys.TAX_BY_TAX_CAT, null, null, null, 0));
		for (TaxCategory cat : taxCategoryList) {
			fields.add(modelFactory.createReportField(false, cat.getBundleKey(),
					salesFigures.taxPerTaxCat().get(cat),
					refundsFigures.taxPerTaxCat().get(cat),
					cat.getBundleName(),
					2
			));
		}
		fields.add(modelFactory.createReportField(true, Keys.TAX_AMOUNT,
				salesFigures.getTaxAmount(),
				refundsFigures.getTaxAmount(),
				null, 2));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCreatedAt() {
		if (type == ReportType.Z_REPORT && record != null) {
			return record.getCreatedAt();
		}
		return createdAt;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getCashBalance() {
		return salesFigures.getCashAmount().subtract(refundsFigures.getCashAmount());
	}
	
	/**
	 * {@inheritDoc}
	 * Returns label if ReportType.Z_REPORT, otherwise null.
	 */
	@Override
	public String getDocType() {
		if (type == ReportType.Z_REPORT) {
			return Keys.FISCAL;
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportField> getFields() {
		return fields;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportSummary getSalesFigures() {
		return salesFigures;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportSummary getRefundsFigures() {
		return refundsFigures;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportType getType() {
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cashbox getCashbox() {
		return cashbox;
	}
	
	/**
	 * {@inheritDoc}
	 * Currently returns zero.
	 */
	@Override
	public BigDecimal getDeposit() {
		return deposit;
	}
	
	/**
	 * {@inheritDoc}
	 * Currently returns zero.
	 */
	@Override
	public BigDecimal getWithdrawal() {
		return withdrawal;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ZReport getRecord() {
		return record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRecord(ZReport record) {
		this.record = record;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getCashboxId() {
		return cashboxId;
	}
	
	private ModelFactory getModelFactory() {
		if (modelFactory == null) {
			modelFactory = Application.getModelFactory();
		}
		return modelFactory;
	}
	
	/**
	 * Returns result of {@code Z-Report} record toString() method.
	 */
	@Override
	public String toString() {
		if (record != null) {
			return record.toString();
		}
		return super.toString();
	}
}
