package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.domain.*;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;

public class TestModelFactory implements ModelFactory {
	
	@Override
	public Product createProduct() {
		return null;
	}
	
	@Override
	public Product createProduct(ProductRecord record) {
		Product product = new ProductMock();
		product.setRecord(record);
		return product;
	}
	
	@Override
	public Receipt createReceipt(ReceiptRecord record) {
		return new ReceiptStub(record);
	}
	
	@Override
	public ReportField createReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
		return new ReportFieldStub(showInList, name, salesValue, refundsValue, bundle, fractionalDigits);
	}
	
	@Override
	public ReportSummary createReportSummary(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		return new ReportSummaryMock(receiptList, taxCats, paymentTypes);
	}
	
	@Override
	public Report createReport(Long userId) {
		return new ReportStub();
	}
	
	@Override
	public Receipt createReceipt(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy) {
		return null;
	}
}
