package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.records.ProductRecord;
import ua.kapitonenko.app.dao.records.ReceiptRecord;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.domain.*;

import java.math.BigDecimal;
import java.util.List;

public class ModelFactoryImpl implements ModelFactory {
	private static ModelFactoryImpl instance = new ModelFactoryImpl();
	
	private ModelFactoryImpl() {
	}
	
	public static ModelFactoryImpl getInstance() {
		return instance;
	}
	
	@Override
	public Product createProduct() {
		return new ProductImpl(new ProductRecord());
	}
	
	@Override
	public Product createProduct(ProductRecord record) {
		return new ProductImpl(record);
	}
	
	@Override
	public Receipt createReceipt(ReceiptRecord record) {
		return new ReceiptImpl(record);
	}
	
	@Override
	public Receipt createReceipt(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		return new ReceiptImpl(cashboxId, paymentTypeId, receiptTypeId, cancelled, createdBy);
	}
	
	@Override
	public Report createReport(Long userId) {
		return new ReportImpl(userId);
	}
	
	@Override
	public ReportField createReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
		return new ReportFieldImpl(showInList, name, salesValue, refundsValue, bundle, fractionalDigits);
	}
	
	@Override
	public ReportSummary createReportSummary(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		return new ReportSummaryImpl(receiptList, taxCats, paymentTypes);
	}
}
