package ua.kapitonenko.app.domain;

import java.math.BigDecimal;

/**
 * {@code ReportField} interface defines methods for grouping information
 * on one specific aspect of report both for sales and refunds. Is used by {@code Report}.
 */
public interface ReportField extends Model {
	
	/**
	 * Returns the name of resource bundle that stores the field label.
	 */
	String getBundle();
	
	/**
	 * Returns the bundle key for field label.
	 */
	String getName();
	
	/**
	 * Returns sales value.
	 */
	BigDecimal getSalesValue();
	
	
	/**
	 * Returns refunds value.
	 */
	BigDecimal getRefundsValue();
	
	/**
	 * Returns number of fractional digits for formatting values.
	 */
	int getFractionalDigits();
	
	/**
	 * Indicates whether field can be rendered in report list view.
	 */
	boolean isShowInList();
}
