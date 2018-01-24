package ua.kapitonenko.app.domain.impl;

import org.apache.commons.lang3.StringUtils;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.ReportField;

import java.math.BigDecimal;

/**
 * Implementation of {@code ReportField} interface.
 */
public class ReportFieldImpl implements ReportField {
	private String name;
	private String bundle = Application.Params.MESSAGE_BUNDLE.getValue();
	private int fractionalDigits;
	private BigDecimal salesValue;
	private BigDecimal refundsValue;
	private boolean showInList;
	
	/**
	 * Constructor initializes all instance fields with given arguments.
	 */
	ReportFieldImpl(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
		this.name = name;
		this.salesValue = salesValue;
		this.refundsValue = refundsValue;
		this.fractionalDigits = fractionalDigits;
		this.showInList = showInList;
		
		if (!StringUtils.isEmpty(bundle)) {
			this.bundle = bundle;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBundle() {
		return bundle;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getSalesValue() {
		return salesValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getRefundsValue() {
		return refundsValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFractionalDigits() {
		return fractionalDigits;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowInList() {
		return showInList;
	}
}
