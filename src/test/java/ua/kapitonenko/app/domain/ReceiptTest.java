package ua.kapitonenko.app.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.fixtures.ModelUtils;
import ua.kapitonenko.app.fixtures.ProductMock;
import ua.kapitonenko.app.fixtures.TestModelFactory;
import ua.kapitonenko.app.persistence.records.ReceiptRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptTest {
	public static final int TAX_CAT_SIZE = 2;
	public static final int PRODUCTS_SIZE = 3;
	
	private Receipt receipt;
	
	private ModelFactory appModelFactory;
	
	@Mock
	private ReceiptRecord record;
	
	
	@Before
	public void setUp() throws Exception {
		appModelFactory = Application.getModelFactory();
		receipt = appModelFactory.createReceipt(record);
		Application.setModelFactory(new TestModelFactory());
	}
	
	@After
	public void tearDown() throws Exception {
		Application.setModelFactory(appModelFactory);
	}
	
	@Test
	public void constructorWith5ArgsShouldInitRecord() throws Exception {
		Long ids = 4L;
		receipt = appModelFactory.createReceipt(ids, ids, ids, false, ids);
		
		ReceiptRecord record = receipt.getRecord();
		assertThat(record.getCashboxId(), is(equalTo(ids)));
		assertThat(record.getPaymentTypeId(), is(equalTo(ids)));
		assertThat(record.getReceiptTypeId(), is(equalTo(ids)));
		assertThat(record.getCreatedBy(), is(equalTo(ids)));
		assertThat(record.isCancelled(), is(false));
	}
	
	@Test
	public void getTypeIdShouldReturnRecordTypeId() throws Exception {
		Long expected = 55L;
		when(record.getReceiptTypeId()).thenReturn(expected);
		
		assertThat(receipt.getTypeId(), is(equalTo(expected)));
	}
	
	@Test
	public void setLocalIdShouldSetLocaleIdFieldAndSetLocaleOnProducts() throws Exception {
		Long localeId = 67L;
		List<Product> products = ModelUtils.generateProductList(2);
		receipt.setProducts(products);
		
		receipt.setLocalId(localeId);
		assertThat(receipt.getLocalId(), is(equalTo(localeId)));
		products.forEach(product -> assertThat(product.getLocaleId(), is(equalTo(localeId))));
	}
	
	@Test
	public void addProductShouldUpdateQuantityOnExistingProduct() throws Exception {
		BigDecimal existingQuantity = BigDecimal.ONE;
		Product existing = new ProductMock();
		existing.setQuantity(existingQuantity);
		
		List<Product> products = new ArrayList<>();
		products.add(existing);
		receipt.setProducts(products);
		
		receipt.addProduct(existing);
		assertThat(receipt.getProducts().size(), is(equalTo(1)));
		assertThat(receipt.getProducts().get(0).getQuantity(), is(equalTo(BigDecimal.valueOf(2))));
	}
	
	@Test
	public void addProductShouldAddNewProductToList() throws Exception {
		int productSize = 4;
		receipt.setProducts(ModelUtils.generateProductList(productSize));
		
		Product newProduct = new ProductMock();
		
		receipt.addProduct(newProduct);
		assertThat(receipt.getProducts().size(), is(equalTo(productSize + 1)));
	}
	
	@Test
	public void removeShouldRemoveProductFromList() throws Exception {
		int productSize = 4;
		List<Product> products = ModelUtils.generateProductList(productSize);
		receipt.setProducts(products);
		
		receipt.remove(products.get(1).getId());
		assertThat(receipt.getProducts().size(), is(equalTo(productSize - 1)));
	}
	
	@Test
	public void getTaxByCategoryShouldReturnMapOfTaxAmountByCat() throws Exception {
		BigDecimal taxPerProduct = BigDecimal.valueOf(7.77);
		
		List<TaxCategory> taxCatList = initAndReturnTaxCats(receipt, taxPerProduct);
		
		Map<TaxCategory, BigDecimal> taxMap = receipt.getTaxByCategory();
		assertThat(taxMap.size(), is(equalTo(taxCatList.size())));
		assertThat(taxMap.get(taxCatList.get(0)), is(equalTo(taxPerProduct.multiply(BigDecimal.valueOf(2)))));
		assertThat(taxMap.get(taxCatList.get(1)), is(equalTo(taxPerProduct)));
	}
	
	@Test
	public void getCostByCategoryShouldReturnMapOfGrossTotalByCat() throws Exception {
		BigDecimal costPerProd = BigDecimal.valueOf(0.77);
		
		List<TaxCategory> taxCatList = initAndReturnTaxCats(receipt, costPerProd);
		
		Map<TaxCategory, BigDecimal> taxMap = receipt.getCostByCategory();
		assertThat(taxMap.size(), is(equalTo(taxCatList.size())));
		assertThat(taxMap.get(taxCatList.get(0)), is(equalTo(costPerProd.multiply(BigDecimal.valueOf(2)))));
		assertThat(taxMap.get(taxCatList.get(1)), is(equalTo(costPerProd)));
	}
	
	@Test
	public void getTotalCost() throws Exception {
		BigDecimal costPerProd = BigDecimal.valueOf(0.77);
		initAndReturnTaxCats(receipt, costPerProd);
		
		assertThat(receipt.getTotalCost(), is(equalTo(costPerProd.multiply(BigDecimal.valueOf(PRODUCTS_SIZE)))));
	}
	
	@Test
	public void getTaxAmount() throws Exception {
		BigDecimal taxPerProduct = BigDecimal.valueOf(0.01);
		initAndReturnTaxCats(receipt, taxPerProduct);
		
		assertThat(receipt.getTaxAmount(), is(equalTo(taxPerProduct.multiply(BigDecimal.valueOf(PRODUCTS_SIZE)))));
	}
	
	@Test
	public void canBeReturnedShouldReturnTrueWhenCashAndNotReturn() throws Exception {
		when(record.getPaymentTypeId()).thenReturn(Application.Ids.PAYMENT_TYPE_CASH.getValue());
		when(record.getReceiptTypeId()).thenReturn(Application.Ids.RECEIPT_TYPE_FISCAL.getValue());
		receipt.setRecord(record);
		
		assertThat(receipt.isReturnAllowed(), is(true));
	}
	
	@Test
	public void canBeReturnedShouldReturnFalseWhenNotCashOrReturn() throws Exception {
		when(record.getPaymentTypeId()).thenReturn(Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue());
		when(record.getReceiptTypeId()).thenReturn(Application.Ids.RECEIPT_TYPE_FISCAL.getValue());
		receipt.setRecord(record);
		
		assertThat(receipt.isReturnAllowed(), is(false));
		
		when(record.getPaymentTypeId()).thenReturn(Application.Ids.PAYMENT_TYPE_CASH.getValue());
		when(record.getReceiptTypeId()).thenReturn(Application.Ids.RECEIPT_TYPE_RETURN.getValue());
		receipt.setRecord(record);
		
		assertThat(receipt.isReturnAllowed(), is(false));
	}
	
	@Test
	public void canBePrintedShouldReturnTrueWhenDefinedPaymentAndProductsNotEmpty() throws Exception {
		receipt.setProducts(ModelUtils.generateProductList(2));
		when(record.getPaymentTypeId()).thenReturn(new Random().nextLong());
		
		assertThat(receipt.isPrintAllowed(), is(true));
	}
	
	@Test
	public void canBePrintedShouldReturnFalseWhenPaymentUndefinedOrProductsEmpty() throws Exception {
		receipt.setProducts(ModelUtils.generateProductList(2));
		when(record.getPaymentTypeId()).thenReturn(Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue());
		
		assertThat(receipt.isPrintAllowed(), is(false));
		
		receipt.setProducts(Collections.emptyList());
		when(record.getPaymentTypeId()).thenReturn(new Random().nextLong());
		
		
	}
	
	private List<TaxCategory> initAndReturnTaxCats(Receipt receipt, BigDecimal amount) {
		
		List<TaxCategory> taxCatList = ModelUtils.generateTaxCatList(TAX_CAT_SIZE);
		
		List<Product> products = ModelUtils.generateProductList(PRODUCTS_SIZE);
		products.forEach(product -> {
			((ProductMock) product).setTax(amount);
			((ProductMock) product).setCost(amount);
		});
		products.get(0).setTaxCategory(taxCatList.get(0));
		products.get(1).setTaxCategory(taxCatList.get(0));
		products.get(2).setTaxCategory(taxCatList.get(1));
		
		
		receipt.setProducts(products);
		receipt.setCategories(taxCatList);
		
		return taxCatList;
	}
	
}