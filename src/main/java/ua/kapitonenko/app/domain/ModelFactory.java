package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;

/**
 * This interface defines a factory for {@link Model} interface implementations.
 */
public interface ModelFactory {
	
	/**
	 * Creates a new {@code Product} implementation instance.
	 */
	Product createProduct();
	
	/**
	 * Creates a new {@code Product} implementation instance with the specified {@code ProductRecord}.
	 */
	Product createProduct(ProductRecord record);
	
	/**
	 * Creates a new {@code Receipt} implementation instance with the specified {@code ReceiptRecord}.
	 */
	Receipt createReceipt(ReceiptRecord record);
	
	/**
	 * Creates a new {@code Receipt} implementation instance with the specified parameters.
	 */
	Receipt createReceipt(Long cashboxId, Long paymentTypeId, Long receiptTypeId, boolean cancelled, Long createdBy);
	
	/**
	 * Creates a new {@code Report} implementation instance
	 * with the specified id of {@code User} record, who created report.
	 */
	Report createReport(Long userId);
	
	/**
	 * Creates a new {@code ReportField} implementation instance with the specified parameters.
	 * Is used by {@link Report}
	 */
	ReportField createReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue,
	                              String bundle, int fractionalDigits);
	
	/**
	 * Creates a new {@code ReportSummary} implementation instance with the specified parameters.
	 * Is used by {@link Report}.
	 */
	ReportSummary createReportSummary(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes);
}
