package ua.kapitonenko.app.domain.impl;

import org.apache.commons.lang3.StringUtils;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.ReportField;

import java.math.BigDecimal;

public class ReportFieldImpl implements ReportField {
	private String name;
	private String bundle = Application.Params.MESSAGE_BUNDLE.getValue();
	private int fractionalDigits;
	private BigDecimal salesValue;
	private BigDecimal refundsValue;
	private boolean showInList;
	
	
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
	
	@Override
	public String getBundle() {
		return bundle;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public BigDecimal getSalesValue() {
		return salesValue;
	}
	
	@Override
	public void setSalesValue(BigDecimal salesValue) {
		this.salesValue = salesValue;
	}
	
	@Override
	public BigDecimal getRefundsValue() {
		return refundsValue;
	}
	
	@Override
	public void setRefundsValue(BigDecimal refundsValue) {
		this.refundsValue = refundsValue;
	}
	
	@Override
	public int getFractionalDigits() {
		return fractionalDigits;
	}
	
	@Override
	public boolean isShowInList() {
		return showInList;
	}
}
