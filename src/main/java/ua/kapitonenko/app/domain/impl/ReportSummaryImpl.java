package ua.kapitonenko.app.domain.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.ReportSummary;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@code ReportSummary} interface. Calculates and returns totals.
 */
public class ReportSummaryImpl implements ReportSummary {
	
	private List<Receipt> receiptList;
	private List<TaxCategory> taxCats;
	private List<PaymentType> paymentTypes;
	private long noCancelled;
	
	/**
	 * Constructor initializes all fields, required for making calculations.
	 */
	ReportSummaryImpl(List<Receipt> receiptList, List<TaxCategory> taxCats, List<PaymentType> paymentTypes) {
		
		noCancelled = receiptList.stream()
				              .filter(receipt -> receipt.getRecord().isCancelled())
				              .count();
		
		this.receiptList = receiptList.stream()
				                   .filter(receipt -> !receipt.getRecord().isCancelled())
				                   .collect(Collectors.toList());
		this.taxCats = taxCats;
		this.paymentTypes = paymentTypes;
		
	}
	
	@Override
	public List<Receipt> getReceiptList() {
		return receiptList;
	}
	
	@Override
	public long getNoReceipts() {
		return receiptList.size();
	}
	
	@Override
	public long getNoCancelled() {
		return noCancelled;
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public long getNoArticles() {
		return receiptList.stream()
				       .map(Receipt::getProducts)
				       .flatMap(Collection::stream)
				       .collect(Collectors.toSet())
				       .size();
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public Map<PaymentType, BigDecimal> costPerPayType() {
		Map<PaymentType, BigDecimal> map = new TreeMap<>();
		for (PaymentType type : paymentTypes) {
			map.put(type, new BigDecimal("0.00"));
		}
		for (Receipt receipt : receiptList) {
			PaymentType type = receipt.getPaymentType();
			map.put(type, map.get(type)
					              .add(receipt.getTotalCost()));
		}
		return map;
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public Map<TaxCategory, BigDecimal> costPerTaxCat() {
		Map<TaxCategory, BigDecimal> map = new TreeMap<>();
		for (TaxCategory cat : taxCats) {
			map.put(cat, new BigDecimal("0.00"));
		}
		
		for (Receipt receipt : receiptList) {
			Map<TaxCategory, BigDecimal> cost = receipt.getCostByCategory();
			for (TaxCategory cat : cost.keySet()) {
				map.put(cat, map.get(cat).add(cost.get(cat)));
			}
		}
		return map;
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public Map<TaxCategory, BigDecimal> taxPerTaxCat() {
		Map<TaxCategory, BigDecimal> map = new TreeMap<>();
		for (TaxCategory cat : taxCats) {
			map.put(cat, new BigDecimal("0.00"));
		}
		
		for (Receipt receipt : receiptList) {
			Map<TaxCategory, BigDecimal> tax = receipt.getTaxByCategory();
			for (TaxCategory cat : tax.keySet()) {
				map.put(cat, map.get(cat).add(tax.get(cat)));
			}
		}
		return map;
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTaxAmount() {
		return receiptList.stream()
				       .map(Receipt::getTaxAmount)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getTotalCost() {
		return receiptList.stream()
				       .map(Receipt::getTotalCost)
				       .reduce(new BigDecimal("0.00"), BigDecimal::add);
		
	}
	
	/**
	 * Calculates and
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getCashAmount() {
		PaymentType cash = paymentTypes.stream()
				                   .filter(paymentType -> paymentType.getId().equals(Application.Ids.PAYMENT_TYPE_CASH.getValue()))
				                   .findFirst().orElseGet(null);
		return costPerPayType().getOrDefault(cash, BigDecimal.ZERO);
	}
	
	
}
