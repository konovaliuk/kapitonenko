package ua.kapitonenko.app.controller.helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.domain.records.Cashbox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationBuilderLogicValidArgsTest {
	
	
	private ValidationBuilder validator;
	
	@Mock
	private MessageProvider messageProvider;
	
	@Mock
	private AlertContainer alert;
	
	@Before
	public void createValidValidationBuilder() {
		validator = new ValidationBuilder(messageProvider, alert);
		assertThat(validator.isValid(), is(true));
	}
	
	@After
	public void assertValid() {
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void parseDecimalInteger() throws Exception {
		validator.parseDecimal("-10", 0, "");
	}
	
	@Test
	public void parseDecimalDouble() throws Exception {
		validator.parseDecimal("9.999", 3, "");
	}
	
	@Test
	public void parseDecimalNaN() throws Exception {
		validator.parseDecimal("one", 3, "");
	}
	
	@Test
	public void requiredString() throws Exception {
		validator.required("yes", "");
	}
	
	@Test
	public void requiredObject() throws Exception {
		validator.required(new Object(), "");
	}
	
	@Test
	public void requiredList() throws Exception {
		validator.required(Arrays.asList(1, 2), "");
	}
	
	@Test
	public void requiredAllLang() throws Exception {
		validator.requiredAllLang(new String[]{"a", "b"}, "");
	}
	
	@Test
	public void requiredEach() throws Exception {
		validator.requiredEach(BigDecimal.ZERO, "");
	}
	
	@Test
	public void requiredOne() throws Exception {
		validator.requiredOne(null, "", "*", "");
	}
	
	@Test
	public void identical() throws Exception {
		validator.identical("---", "", "---", "");
	}
	
	@Test
	public void idInSet() throws Exception {
		validator.idInSet(444L, new HashSet<>(Arrays.asList(1L, 444L)), "");
	}
	
	@Test
	public void idInList() throws Exception {
		Cashbox cashbox = mock(Cashbox.class);
		when(cashbox.getId()).thenReturn(444L);
		List<Cashbox> list = new ArrayList<>();
		list.add(cashbox);
		
		validator.idInList(444L, list, "");
	}
	
	@Test
	public void unique() throws Exception {
		validator.unique(null, "");
	}
	
	@Test
	public void exists() throws Exception {
		validator.exists(37L, () -> true, "");
	}
	
	@Test
	public void listSize() throws Exception {
		validator.listSize(2, Arrays.asList(1, 2), "", "");
	}
	
	@Test
	public void notLessMessageEqual() throws Exception {
		validator.notLess(BigDecimal.valueOf(3.33), BigDecimal.valueOf(3.33), "message");
	}
	
	@Test
	public void notLessMessageNegative() throws Exception {
		validator.notLess(BigDecimal.valueOf(-99), BigDecimal.valueOf(-100), "message");
	}
	
	@Test
	public void notLessMessagePositive() throws Exception {
		validator.notLess(BigDecimal.valueOf(100), BigDecimal.valueOf(99), "message");
	}
	
	@Test
	public void notLessMessageZero() throws Exception {
		validator.notLess(BigDecimal.valueOf(1), BigDecimal.ZERO, "message");
	}
	
	@Test
	public void notLessFormatEqual() throws Exception {
		validator.notLess(BigDecimal.valueOf(3.33), BigDecimal.valueOf(3.33), "format", "message");
	}
	
	@Test
	public void notLessFormatNegative() throws Exception {
		validator.notLess(BigDecimal.valueOf(-99), BigDecimal.valueOf(-100), "format", "message");
	}
	
	@Test
	public void notLessFormatPositive() throws Exception {
		validator.notLess(BigDecimal.valueOf(100), BigDecimal.valueOf(99), "format", "message");
	}
	
	@Test
	public void notLessFormatZero() throws Exception {
		validator.notLess(BigDecimal.valueOf(1), BigDecimal.ZERO, "format", "message");
	}
	
	@Test
	public void notGreaterMessageEqual() throws Exception {
		validator.notGreater(BigDecimal.valueOf(3.33), BigDecimal.valueOf(3.33), "message");
	}
	
	@Test
	public void notGreaterMessageNegative() throws Exception {
		validator.notGreater(BigDecimal.valueOf(-100), BigDecimal.valueOf(-99), "message");
	}
	
	@Test
	public void notGreaterMessagePositive() throws Exception {
		validator.notGreater(BigDecimal.valueOf(99), BigDecimal.valueOf(100), "message");
	}
	
	@Test
	public void notGreaterMessageZero() throws Exception {
		validator.notGreater(BigDecimal.valueOf(-1), BigDecimal.ZERO, "message");
	}
	
	@Test
	public void notGreaterFormatEqual() throws Exception {
		validator.notGreater(BigDecimal.valueOf(1.111), BigDecimal.valueOf(1.111), "format", "message");
	}
	
	@Test
	public void notGreaterFormatNegative() throws Exception {
		validator.notGreater(BigDecimal.valueOf(-1000), BigDecimal.valueOf(-999), "format", "message");
	}
	
	@Test
	public void notGreaterFormatPositive() throws Exception {
		validator.notGreater(BigDecimal.valueOf(55.554), BigDecimal.valueOf(55.555), "format", "message");
	}
	
	@Test
	public void notGreaterFormatZero() throws Exception {
		validator.notGreater(BigDecimal.ZERO, BigDecimal.valueOf(1), "format", "message");
	}
}
