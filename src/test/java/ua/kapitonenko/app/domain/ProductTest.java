package ua.kapitonenko.app.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.persistence.records.LocaleRecord;
import ua.kapitonenko.app.persistence.records.ProductLocale;
import ua.kapitonenko.app.persistence.records.ProductRecord;
import ua.kapitonenko.app.persistence.records.TaxCategory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductTest {
	private Product product;
	
	@Mock
	private ProductRecord record;
	
	@Mock
	private LocaleRecord localeRecord;
	
	@Mock
	private ProductLocale activeLang;
	
	@Mock
	private ProductLocale inactiveLang;
	
	@Mock
	private TaxCategory taxCategory;
	
	@Before
	public void setUp() throws Exception {
		product = Application.getModelFactory().createProduct(record);
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void initNamesShouldFillNamesListWithSupportedLang() throws Exception {
		List<LocaleRecord> supportedLang = Arrays.asList(localeRecord, localeRecord, localeRecord);
		
		product.initNames(supportedLang);
		
		assertThat(product.getNames().size(), is(equalTo(3)));
	}
	
	@Test
	public void getNameShouldReturnNameForActiveLocale() throws Exception {
		Long activeLocale = 4L;
		String expectedName = "mleko";
		
		when(activeLang.getLocaleId()).thenReturn(activeLocale);
		when(activeLang.getPropertyValue()).thenReturn(expectedName);
		when(activeLang.getPropertyName()).thenReturn(Keys.PRODUCT_NAME);
		
		when(inactiveLang.getLocaleId()).thenReturn(new Random().nextLong());
		when(inactiveLang.getPropertyName()).thenReturn(Keys.PRODUCT_NAME);
		when(inactiveLang.getPropertyValue()).thenReturn("");
		
		List<ProductLocale> names = Arrays.asList(activeLang, inactiveLang);
		product.setNames(names);
		product.setLocaleId(activeLocale);
		
		assertThat(product.getName(), is(equalTo(expectedName)));
	}
	
	@Test
	public void fillNamesShouldReplaceNamesInList() throws Exception {
		List<ProductLocale> names = Arrays.asList(activeLang, inactiveLang);
		product.setNames(names);
		
		product.fillNames(new String[]{"milk", "sugar"});
		
		verify(activeLang).setPropertyValue("milk");
		verify(inactiveLang).setPropertyValue("sugar");
	}
	
	
	@Test
	public void getCostShouldReturnGrossTotalScale2() throws Exception {
		BigDecimal price = BigDecimal.valueOf(90);
		BigDecimal quantity = BigDecimal.valueOf(4.333);
		when(record.getPrice()).thenReturn(price);
		when(record.getQuantity()).thenReturn(quantity);
		
		assertThat(product.getCost(), is(not(equalTo(price.multiply(quantity)))));
		assertThat(product.getCost(), is(equalTo(price.multiply(quantity).stripTrailingZeros())));
	}
	
	@Test
	public void getTaxShouldReturnTaxAmountScale2() throws Exception {
		BigDecimal price = BigDecimal.valueOf(50);
		BigDecimal quantity = BigDecimal.valueOf(3);
		BigDecimal taxRate = BigDecimal.valueOf(7);
		BigDecimal expectedTax = new BigDecimal("10.50");
		when(record.getPrice()).thenReturn(price);
		when(record.getQuantity()).thenReturn(quantity);
		when(taxCategory.getRate()).thenReturn(taxRate);
		
		product.setTaxCategory(taxCategory);
		assertThat(product.getTax(), is(equalTo(expectedTax)));
	}
	
	@Test
	public void addQuantityShouldChangeRecordQuantity() throws Exception {
		when(record.getQuantity()).thenReturn(BigDecimal.ONE);
		product.addQuantity(BigDecimal.ONE);
		verify(record).setQuantity(BigDecimal.valueOf(2));
	}
	
	@Test
	public void equalsShouldCompareProductsByRecord() throws Exception {
		ProductRecord otherRecord = mock(ProductRecord.class);
		assertThat(record.equals(otherRecord), is(false));
		
		Product other = Application.getModelFactory().createProduct(otherRecord);
		assertThat(product.equals(other), is(false));
		assertThat(product.hashCode(), is(not(equalTo(other.hashCode()))));
		
	}
}