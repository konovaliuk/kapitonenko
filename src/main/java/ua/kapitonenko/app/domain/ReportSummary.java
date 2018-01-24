package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * {@code ReportSummary} defines interface  for getting totals grouped by sales or refunds operations.
 * Is used by {@code Report}.
 */
public interface ReportSummary extends Model {
	
	/**
	 * Returns the list of {@code Receipt} implementation instances,
	 * for which the total figures are calculated.
	 */
	List<Receipt> getReceiptList();
	
	/**
	 * Returns the size of ReceiptList.
	 */
	long getNoReceipts();
	
	/**
	 * Returns the number of cancelled Receipts in ReceiptList
	 */
	long getNoCancelled();
	
	/**
	 * Returns the number of distinct Products in Receipts.
	 */
	long getNoArticles();
	
	/**
	 * The gross total grouped by {@link PaymentType} record.
	 */
	Map<PaymentType, BigDecimal> costPerPayType();
	
	/**
	 * The gross total grouped by {@link TaxCategory} record.
	 */
	Map<TaxCategory, BigDecimal> costPerTaxCat();
	
	/**
	 * The tax amount grouped by {@link TaxCategory} record.
	 */
	Map<TaxCategory, BigDecimal> taxPerTaxCat();
	
	/**
	 * Returns total tax amount.
	 */
	BigDecimal getTaxAmount();
	
	/**
	 * Returns gross total.
	 */
	BigDecimal getTotalCost();
	
	/**
	 * Returns gross total only for records where {@link PaymentType} equals Cash.
	 */
	BigDecimal getCashAmount();
}
