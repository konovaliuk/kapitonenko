package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.domain.*;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Singleton implementation of {@code ModelFactory} interface
 */
public class ModelFactoryImpl implements ModelFactory {
	private static ModelFactoryImpl instance = new ModelFactoryImpl();
	
	private ModelFactoryImpl() {
	}
	
	/**
	 * Returns the single instance of {@code ModelFactoryImpl}
	 */
	public static ModelFactoryImpl getInstance() {
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Product createProduct() {
		return new ProductImpl(new ProductRecord());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Product createProduct(ProductRecord record) {
		return new ProductImpl(record);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Receipt createReceipt(ReceiptRecord record) {
		return new ReceiptImpl(record);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Receipt createReceipt(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		return new ReceiptImpl(cashboxId, paymentTypeId, receiptTypeId, cancelled, createdBy);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report createReport(Long userId) {
		return new ReportImpl(userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportField createReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
		return new ReportFieldImpl(showInList, name, salesValue, refundsValue, bundle, fractionalDigits);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportSummary createReportSummary(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		return new ReportSummaryImpl(receiptList, taxCats, paymentTypes);
	}
}
