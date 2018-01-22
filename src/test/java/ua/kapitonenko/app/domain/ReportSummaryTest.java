package ua.kapitonenko.app.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.fixtures.ProductMock;
import ua.kapitonenko.app.fixtures.ReceiptStub;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReportSummaryTest {
	
	private ReportSummary reportSummary;
	
	private ModelFactory appModelFactory;
	
	@Before
	public void setUp() throws Exception {
		appModelFactory = Application.getModelFactory();
		Application.setModelFactory(new TestModelFactory());
	}
	
	@After
	public void tearDown() throws Exception {
		Application.setModelFactory(appModelFactory);
	}
	
	@Test
	public void getNoReceiptsShouldReturnNoOfNotCancelledReceipts() throws Exception {
		long active = 2;
		long cancelled = 6;
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(active + cancelled, cancelled, true),
				Collections.emptyList(), Collections.emptyList());
		
		assertThat(reportSummary.getNoReceipts(), is(equalTo(active)));
	}
	
	@Test
	public void getNoCancelled() throws Exception {
		long active = 8;
		long cancelled = 2;
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(active + cancelled, cancelled, true),
				Collections.emptyList(), Collections.emptyList());
		
		assertThat(reportSummary.getNoCancelled(), is(equalTo(cancelled)));
	}
	
	@Test
	public void getNoArticlesShouldReturnNumberOfDistinctProducts() throws Exception {
		int prodPerReceipt = 3;
		long receiptSize = 2;
		
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(receiptSize),
				Collections.emptyList(), Collections.emptyList());
		
		List<Product> products = Stream.generate(ProductMock::new).limit(prodPerReceipt).collect(Collectors.toList());
		
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			receipt.setProducts(products);
		});
		
		assertThat(reportSummary.getNoArticles(), is(equalTo((long) prodPerReceipt)));
	}
	
	@Test
	public void costPerPayTypeShouldReturnMap() throws Exception {
		BigDecimal costPerReceipt = BigDecimal.valueOf(70.13);
		int cashReceiptsCount = 3;
		int otherTypeCount = 2;
		List<PaymentType> paymentTypeList = ModelUtils.generatePayTypesList();
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generatePayTypeReceiptList(cashReceiptsCount, otherTypeCount),
				Collections.emptyList(), paymentTypeList);
		
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			((ReceiptStub) receipt).setTotalCost(costPerReceipt);
		});
		
		Map<PaymentType, BigDecimal> map = reportSummary.costPerPayType();
		
		assertThat(map.get(paymentTypeList.get(0)), is(equalTo(costPerReceipt.multiply(BigDecimal.valueOf(cashReceiptsCount)))));
		assertThat(map.get(paymentTypeList.get(1)), is(equalTo(costPerReceipt.multiply(BigDecimal.valueOf(otherTypeCount)))));
	}
	
	@Test
	public void costPerTaxCatShouldReturnMap() throws Exception {
		int taxCatSize = 2;
		int receiptListSize = 2;
		BigDecimal costPerCat = BigDecimal.valueOf(100.01);
		List<TaxCategory> taxCategories = initSummaryTaxCats(taxCatSize, receiptListSize, costPerCat);
		
		Map<TaxCategory, BigDecimal> map = reportSummary.costPerTaxCat();
		
		assertThat(map.size(), is(equalTo(taxCatSize)));
		assertThat(map.get(taxCategories.get(0)), is(equalTo(costPerCat.multiply(BigDecimal.valueOf(receiptListSize)))));
		assertThat(map.get(taxCategories.get(1)), is(equalTo(costPerCat.multiply(BigDecimal.valueOf(receiptListSize)))));
		
	}
	
	@Test
	public void taxPerTaxCatShouldReturnMap() throws Exception {
		int taxCatSize = 4;
		int receiptListSize = 3;
		BigDecimal taxPerCat = BigDecimal.valueOf(0.01);
		List<TaxCategory> taxCategories = initSummaryTaxCats(taxCatSize, receiptListSize, taxPerCat);
		
		Map<TaxCategory, BigDecimal> map = reportSummary.taxPerTaxCat();
		
		assertThat(map.size(), is(equalTo(taxCatSize)));
		assertThat(map.get(taxCategories.get(0)), is(equalTo(taxPerCat.multiply(BigDecimal.valueOf(receiptListSize)))));
		assertThat(map.get(taxCategories.get(1)), is(equalTo(taxPerCat.multiply(BigDecimal.valueOf(receiptListSize)))));
	}
	
	private List<TaxCategory> initSummaryTaxCats(int taxCatSize, int receiptListSize, BigDecimal amount) {
		List<TaxCategory> taxCategories = ModelUtils.generateTaxCatList(taxCatSize);
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(receiptListSize),
				taxCategories, Collections.emptyList());
		Map<TaxCategory, BigDecimal> amountByCat = taxCategories.stream()
				                                           .collect(Collectors.toMap(Function.identity(), t -> amount));
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			((ReceiptStub) receipt).setCostByCat(amountByCat);
			((ReceiptStub) receipt).setTaxByCat(amountByCat);
		});
		
		return taxCategories;
	}
	
	@Test
	public void getTaxAmountShouldReturnSumOfTaxAmountPerReceipt() throws Exception {
		BigDecimal taxPerReceipt = BigDecimal.valueOf(99.99);
		int receiptsSize = 4;
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(receiptsSize, 0, true),
				Collections.emptyList(), Collections.emptyList());
		
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			((ReceiptStub) receipt).setTaxAmount(taxPerReceipt);
		});
		
		assertThat(reportSummary.getTaxAmount(), is(equalTo(taxPerReceipt.multiply(BigDecimal.valueOf(receiptsSize)))));
	}
	
	@Test
	public void getTotalCostShouldReturnSumOfTotalCostsPerReceipt() throws Exception {
		BigDecimal costPerReceipt = BigDecimal.valueOf(33.33);
		int receiptsSize = 3;
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generateReceiptList(receiptsSize, 0, true),
				Collections.emptyList(), Collections.emptyList());
		
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			((ReceiptStub) receipt).setTotalCost(costPerReceipt);
		});
		
		assertThat(reportSummary.getTotalCost(), is(equalTo(costPerReceipt.multiply(BigDecimal.valueOf(receiptsSize)))));
	}
	
	@Test
	public void getCashAmountShouldReturnSumOfTotalCostsPerCashReceipts() throws Exception {
		BigDecimal costPerReceipt = BigDecimal.valueOf(70.13);
		int cashReceiptsCount = 3;
		
		reportSummary = appModelFactory.createReportSummary(ModelUtils.generatePayTypeReceiptList(cashReceiptsCount, 2),
				Collections.emptyList(), ModelUtils.generatePayTypesList());
		
		List<Receipt> receipts = reportSummary.getReceiptList();
		receipts.forEach(receipt -> {
			((ReceiptStub) receipt).setTotalCost(costPerReceipt);
		});
		
		assertThat(reportSummary.getCashAmount(), is(equalTo(costPerReceipt.multiply(BigDecimal.valueOf(cashReceiptsCount)))));
	}
	
}