package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.records.TaxCategory;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.ReportSummary;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReportSummaryMock implements ReportSummary {
	
	private List<Receipt> receiptList;
	private BigDecimal cashAmount;
	
	private int costPerTaxCatCalls;
	private int taxPerTaxCatCalls;
	private int costPerPayTypeCalls;
	
	public ReportSummaryMock(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		this.receiptList = receiptList;
	}
	
	@Override
	public Map<PaymentType, BigDecimal> costPerPayType() {
		costPerPayTypeCalls++;
		return new HashMap<>();
	}
	
	@Override
	public Map<TaxCategory, BigDecimal> costPerTaxCat() {
		costPerTaxCatCalls++;
		return new HashMap<>();
	}
	
	@Override
	public Map<TaxCategory, BigDecimal> taxPerTaxCat() {
		taxPerTaxCatCalls++;
		return new HashMap<>();
	}
	
	public void setCashAmount(BigDecimal amount) {
		cashAmount = amount;
	}
	
	@Override
	public BigDecimal getCashAmount() {
		return cashAmount;
	}
	
	public void verifyTaxCatCalls(int times) {
		assertThat(taxPerTaxCatCalls, is(equalTo(times)));
		assertThat(costPerTaxCatCalls, is(equalTo(times)));
	}
	
	public void verifyPayTypeCalls(int times) {
		assertThat(costPerPayTypeCalls, is(equalTo(times)));
	}
	
	@Override
	public List<Receipt> getReceiptList() {
		return null;
	}
	
	@Override
	public long getNoReceipts() {
		return receiptList.size();
	}
	
	
	@Override
	public long getNoCancelled() {
		return 0;
	}
	
	@Override
	public long getNoArticles() {
		return 0;
	}
	
	@Override
	public BigDecimal getTaxAmount() {
		return null;
	}
	
	@Override
	public BigDecimal getTotalCost() {
		return null;
	}
	
}
