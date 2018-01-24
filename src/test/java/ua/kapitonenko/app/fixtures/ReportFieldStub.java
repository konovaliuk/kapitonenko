package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.domain.ReportField;

import java.math.BigDecimal;

public class ReportFieldStub implements ReportField {
	
	public ReportFieldStub(boolean showInList, String name, BigDecimal salesValue, BigDecimal refundsValue, String bundle, int fractionalDigits) {
	
	}
	
	@Override
	public String getBundle() {
		return null;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public BigDecimal getSalesValue() {
		return null;
	}
	
	@Override
	public BigDecimal getRefundsValue() {
		return null;
	}
	
	@Override
	public int getFractionalDigits() {
		return 0;
	}
	
	@Override
	public boolean isShowInList() {
		return false;
	}
}
