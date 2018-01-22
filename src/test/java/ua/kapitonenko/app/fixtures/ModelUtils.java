package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.domain.Product;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.persistence.records.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelUtils {
	
	private static ModelFactory testModelFactory = new TestModelFactory();
	
	private static ReceiptRecord firstKind = mock(ReceiptRecord.class);
	
	private static ReceiptRecord secondKind = mock(ReceiptRecord.class);
	
	private static PaymentType firstPaymentType = mock(PaymentType.class);
	
	private static PaymentType secondPaymentType = mock(PaymentType.class);
	
	
	public static List<Product> generateProductList(int size) {
		List<Product> list = Stream.generate(ProductMock::new).limit(size).collect(Collectors.toList());
		assertThat(list.size(), is(equalTo(size)));
		return list;
	}
	
	public static List<TaxCategory> generateTaxCatList(int size) {
		List<TaxCategory> list = Stream.generate(() -> {
			TaxCategory cat = new TaxCategory();
			cat.setId(new Random().nextLong());
			cat.setRate(BigDecimal.valueOf(Math.random()));
			return cat;
		}).limit(size).collect(Collectors.toList());
		assertThat(list.size(), is(equalTo(size)));
		return list;
	}
	
	public static List<PaymentType> generatePayTypesList(int size) {
		List<PaymentType> list = Stream.generate(PaymentType::new).limit(size).collect(Collectors.toList());
		assertThat(list.size(), is(equalTo(size)));
		return list;
	}
	
	public static List<PaymentType> generatePayTypesList() {
		when(firstPaymentType.getId()).thenReturn(Application.Ids.PAYMENT_TYPE_CASH.getValue());
		when(secondPaymentType.getId()).thenReturn(Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue());
		
		List<PaymentType> paymentTypeList = Arrays.asList(firstPaymentType, secondPaymentType);
		return paymentTypeList;
	}
	
	public static List<Receipt> generateReceiptList(long size) {
		return generateReceiptList(size, 0);
	}
	
	public static List<Receipt> generateReceiptList(long salesNo, long refundsNo) {
		when(firstKind.getReceiptTypeId()).thenReturn(Application.Ids.RECEIPT_TYPE_FISCAL.getValue());
		when(firstKind.getId()).thenReturn(new Random().nextLong());
		
		when(secondKind.getReceiptTypeId()).thenReturn(Application.Ids.RECEIPT_TYPE_RETURN.getValue());
		when(secondKind.getId()).thenReturn(new Random().nextLong());
		
		List<Receipt> sales = Stream.generate(() -> testModelFactory.createReceipt(firstKind)).limit(salesNo).collect(Collectors.toList());
		List<Receipt> refunds = Stream.generate(() -> testModelFactory.createReceipt(secondKind)).limit(refundsNo).collect(Collectors.toList());
		List<Receipt> receipts = new ArrayList<>(sales);
		receipts.addAll(refunds);
		return receipts;
	}
	
	public static List<Receipt> generateReceiptList(long totalNo, long cancelledNo, boolean sameType) {
		when(firstKind.isCancelled()).thenReturn(false);
		when(firstKind.getId()).thenReturn(new Random().nextLong());
		
		when(secondKind.isCancelled()).thenReturn(true);
		when(secondKind.getId()).thenReturn(new Random().nextLong());
		
		List<Receipt> active = Stream.generate(() -> testModelFactory.createReceipt(firstKind)).limit(totalNo - cancelledNo).collect(Collectors.toList());
		List<Receipt> cancelled = Stream.generate(() -> testModelFactory.createReceipt(secondKind)).limit(cancelledNo).collect(Collectors.toList());
		List<Receipt> receipts = new ArrayList<>(active);
		receipts.addAll(cancelled);
		return receipts;
	}
	
	public static List<Receipt> generatePayTypeReceiptList(long cashNo, long otherNo) {
		when(firstKind.getId()).thenReturn(new Random().nextLong());
		
		
		List<Receipt> cash = Stream.iterate(testModelFactory.createReceipt(firstKind),
				receipt -> {
					receipt.setPaymentType(firstPaymentType);
					return receipt;
				}).limit(cashNo).collect(Collectors.toList());
		
		List<Receipt> other = Stream.iterate(testModelFactory.createReceipt(firstKind),
				receipt -> {
					receipt.setPaymentType(secondPaymentType);
					return receipt;
				}).limit(otherNo).collect(Collectors.toList());
		
		List<Receipt> receipts = new ArrayList<>(cash);
		receipts.addAll(other);
		assertThat(receipts.size(), is(equalTo((int) (cashNo + otherNo))));
		return receipts;
	}
	
	public static List<ProductLocale> generateProductLocaleList(int size) {
		List<ProductLocale> list = Stream.generate(
				() -> new ProductLocale(new ProductRecord(), new LocaleRecord(), "", ""))
				                           .limit(size)
				                           .collect(Collectors.toList());
		assertThat(list.size(), is(equalTo(size)));
		return list;
	}
	
	public static Long anyLong() {
		return new Random().nextLong();
	}
}
