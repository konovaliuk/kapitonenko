package ua.kapitonenko.app.domain;

import java.math.BigDecimal;

public interface ReportField extends Model {
	String getBundle();
	
	String getName();
	
	void setName(String name);
	
	BigDecimal getSalesValue();
	
	void setSalesValue(BigDecimal salesValue);
	
	BigDecimal getRefundsValue();
	
	void setRefundsValue(BigDecimal refundsValue);
	
	int getFractionalDigits();
	
	boolean isShowInList();
}
