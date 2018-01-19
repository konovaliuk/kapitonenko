package ua.kapitonenko.app.controller.helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.dao.records.Cashbox;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationBuilderLogicInvalidArgsTest {
	
	
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
	public void assertInvalid() {
		assertThat(validator.isValid(), is(false));
	}
	
	@Test
	public void parseDecimalDouble() throws Exception {
		validator.parseDecimal("9.9991", 3, "");
	}
	
	@Test
	public void requiredString() throws Exception {
		validator.required("", "");
	}
	
	@Test
	public void requiredObject() throws Exception {
		Object o = null;
		validator.required(o, "");
	}
	
	@Test
	public void requiredList() throws Exception {
		validator.required(Collections.emptyList(), "");
	}
	
	@Test
	public void requiredAllLang() throws Exception {
		validator.requiredAllLang(new String[2], "");
	}
	
	@Test
	public void requiredEach() throws Exception {
		validator.requiredEach(null, "");
	}
	
	@Test
	public void requiredOne() throws Exception {
		validator.requiredOne(null, "", "", "");
	}
	
	@Test
	public void identical() throws Exception {
		validator.identical("-_-", "", "---", "");
	}
	
	@Test
	public void idInSet() throws Exception {
		validator.idInSet(444L, new HashSet<>(Arrays.asList(1L, 44L)), "");
	}
	
	@Test
	public void idInList() throws Exception {
		Cashbox cashbox = mock(Cashbox.class);
		when(cashbox.getId()).thenReturn(444L);
		List<Cashbox> list = new ArrayList<>();
		list.add(cashbox);
		
		validator.idInList(445L, list, "");
	}
	
	@Test
	public void unique() throws Exception {
		validator.unique(Collections.emptyList(), "");
	}
	
	@Test
	public void exists() throws Exception {
		validator.exists(37L, () -> false, "");
	}
	
	@Test
	public void listSizeEmpty() throws Exception {
		validator.listSize(2, Collections.emptyList(), "", "");
	}
	
	@Test
	public void listSizeGreater() throws Exception {
		validator.listSize(1, Arrays.asList(1, 2), "", "");
	}
	
	@Test
	public void notLessMessageNegative() throws Exception {
		validator.notLess(BigDecimal.valueOf(-99.99), BigDecimal.valueOf(99), "message");
	}
	
	@Test
	public void notLessMessagePositive() throws Exception {
		validator.notLess(BigDecimal.valueOf(100), BigDecimal.valueOf(100.01), "message");
	}
	
	@Test
	public void notLessMessageZero() throws Exception {
		validator.notLess(BigDecimal.valueOf(-0.01), BigDecimal.ZERO, "message");
	}
	
	@Test
	public void notLessFormatNegative() throws Exception {
		validator.notLess(BigDecimal.valueOf(-99), BigDecimal.valueOf(-98), "format", "message");
	}
	
	@Test
	public void notLessFormatPositive() throws Exception {
		validator.notLess(BigDecimal.valueOf(799.98), BigDecimal.valueOf(799.99), "format", "message");
	}
	
	@Test
	public void notLessFormatZero() throws Exception {
		validator.notLess(BigDecimal.valueOf(-0.5), BigDecimal.ZERO, "format", "message");
	}
	
	
	@Test
	public void notGreaterMessageNegative() throws Exception {
		validator.notGreater(BigDecimal.valueOf(-100), BigDecimal.valueOf(-101), "message");
	}
	
	@Test
	public void notGreaterMessagePositive() throws Exception {
		validator.notGreater(BigDecimal.valueOf(44.777), BigDecimal.valueOf(44.776), "message");
	}
	
	@Test
	public void notGreaterMessageZero() throws Exception {
		validator.notGreater(BigDecimal.valueOf(0.1), BigDecimal.ZERO, "message");
	}
	
	@Test
	public void notGreaterFormatNegative() throws Exception {
		validator.notGreater(BigDecimal.valueOf(-1000), BigDecimal.valueOf(-1001), "format", "message");
	}
	
	@Test
	public void notGreaterFormatPositive() throws Exception {
		validator.notGreater(BigDecimal.valueOf(55.554), BigDecimal.valueOf(55.553), "format", "message");
	}
	
	@Test
	public void notGreaterFormatZero() throws Exception {
		validator.notGreater(BigDecimal.ZERO, BigDecimal.valueOf(-1), "format", "message");
	}
}
