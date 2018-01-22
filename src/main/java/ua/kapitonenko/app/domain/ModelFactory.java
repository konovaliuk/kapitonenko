package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;

public interface ModelFactory {
	
	Product createProduct();
	
	Product createProduct(ProductRecord record);
	
	Receipt createReceipt(ReceiptRecord record);
	
	Receipt createReceipt(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy);
	
	Report createReport(Long userId);
	
	ReportField createReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits);
	
	ReportSummary createReportSummary(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes);
}
