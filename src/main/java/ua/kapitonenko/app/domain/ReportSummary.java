package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.dao.records.PaymentType;
import ua.kapitonenko.app.dao.records.TaxCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportSummary extends Model {
	List<Receipt> getReceiptList();
	
	long getNoReceipts();
	
	long getNoCancelled();
	
	long getNoArticles();
	
	Map<PaymentType, BigDecimal> costPerPayType();
	
	Map<TaxCategory, BigDecimal> costPerTaxCat();
	
	Map<TaxCategory, BigDecimal> taxPerTaxCat();
	
	BigDecimal getTaxAmount();
	
	BigDecimal getTotalCost();
	
	BigDecimal getCashAmount();
}
