package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * {@code Report} interface defines methods for generating all types of reports, listed in {@link ReportType}.
 * For ReportType.Z_REPORT it stores information in {@link ZReport} record.
 * For ReportType.X_REPORT it information is stored only in memory.
 */
public interface Report extends Model {
	
	/**
	 * Creates instances of helper classes.
	 * Has to be called first after instantiation in order to provide data for later calculations.
	 */
	void initSummary(List<Receipt> receipts,
	                 List<TaxCategory> taxCats, List<PaymentType> paymentTypes);
	
	/**
	 * Returns the id of {@link User}, which created the Report.
	 */
	Long getUserId();
	
	/**
	 * Returns {@code ReportSummary} on sales operations.
	 */
	ReportSummary getSalesFigures();
	
	/**
	 * Returns {@code ReportSummary} on refunds operations.
	 */
	ReportSummary getRefundsFigures();
	
	/**
	 * Returns list of {@code ReportField}.
	 */
	List<ReportField> getFields();
	
	/**
	 * Returns the date of creation.
	 */
	Date getCreatedAt();
	
	/**
	 * Returns {@code ReportType}.
	 */
	ReportType getType();
	
	/**
	 * Sets {@code ReportType}.
	 */
	void setType(ReportType type);
	
	/**
	 * Returns the {@code Cashbox} for which the report is generated.
	 */
	Cashbox getCashbox();
	
	/**
	 * Sets the {@code Cashbox} for which the report is generated.
	 */
	void setCashbox(Cashbox cashbox);
	
	/**
	 * Returns the amount of cash received by the cashier not on sales transactions.
	 * Function of depositing currently not implemented,
	 * but figure is required for Z-Tape report by Ukrainian law.
	 */
	BigDecimal getDeposit();
	
	/**
	 * Returns the amount of cash withdrawn from the cashier not on refunds transactions.
	 * Function of withdrawal currently not implemented,
	 * but figure is required for Z-Tape report by Ukrainian law.
	 */
	BigDecimal getWithdrawal();
	
	/**
	 * Returns the amount of cash in drawer at the time of report generation.
	 */
	BigDecimal getCashBalance();
	
	/**
	 * Label "Fiscal Receipt" is required for Z-Tape report by Ukrainian law.
	 */
	String getDocType();
	
	/**
	 * Returns {@code ZReport} record.
	 */
	ZReport getRecord();
	
	/**
	 * Sets {@code ZReport} record.
	 */
	void setRecord(ZReport record);
	
	/**
	 * Sets the {@link Cashbox} record id.
	 */
	void setCashboxId(Long cashboxId);
	
	/**
	 * Returns the {@link Cashbox} record id.
	 */
	Long getCashboxId();
}
