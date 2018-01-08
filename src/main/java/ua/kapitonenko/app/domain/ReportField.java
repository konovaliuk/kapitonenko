package ua.kapitonenko.app.domain;

import org.apache.commons.lang3.StringUtils;
import ua.kapitonenko.app.config.Application;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportField extends Model implements Serializable {
	private String name;
	private String bundle = Application.getParam(Application.MESSAGE_BUNDLE);
	private int fractionalDigits;
	private BigDecimal salesValue;
	private BigDecimal refundsValue;
	private boolean showInList;
	
	public ReportField() {
	}
	
	public ReportField(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
		this.name = name;
		this.salesValue = salesValue;
		this.refundsValue = refundsValue;
		this.fractionalDigits = fractionalDigits;
		this.showInList = showInList;
		
		if (!StringUtils.isEmpty(bundle)) {
			this.bundle = bundle;
		}
	}
	
	public String getBundle() {
		return bundle;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getSalesValue() {
		return salesValue;
	}
	
	public void setSalesValue(BigDecimal salesValue) {
		this.salesValue = salesValue;
	}
	
	public BigDecimal getRefundsValue() {
		return refundsValue;
	}
	
	public void setRefundsValue(BigDecimal refundsValue) {
		this.refundsValue = refundsValue;
	}
	
	public int getFractionalDigits() {
		return fractionalDigits;
	}
	
	public boolean isShowInList() {
		return showInList;
	}
}
