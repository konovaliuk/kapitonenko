package ua.kapitonenko.app.controller.helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.kapitonenko.app.domain.records.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValidationBuilderValidValidatorPerformsCheckTest {
	
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
	public void assertAlertChanged() {
		verify(alert, times(1)).addMessage(anyString());
	}
	
	@Test
	public void requiredString() throws Exception {
		validator
				.ifValid()
				.required("", "label");
	}
	
	@Test
	public void requiredObject() throws Exception {
		Object object = null;
		validator
				.ifValid()
				.required(object, "label");
	}
	
	@Test
	public void requiredList() throws Exception {
		List list = null;
		validator
				.ifValid()
				.required(list, "label");
	}
	
	@Test
	public void requiredAllLang() throws Exception {
		validator
				.ifValid()
				.requiredAllLang(new String[]{""}, "label");
	}
	
	@Test
	public void requiredEach() throws Exception {
		validator
				.ifValid()
				.requiredEach(null, "label")
				.isValid();
	}
	
	@Test
	public void requiredOne() throws Exception {
		validator
				.ifValid()
				.requiredOne(null, "", null, "")
				.isValid();
	}
	
	@Test
	public void identical() throws Exception {
		validator
				.ifValid()
				.identical("one", "", "two", "")
				.isValid();
	}
	
	@Test
	public void idInSet() throws Exception {
		validator
				.ifValid()
				.idInSet(1L, new HashSet<>(Arrays.asList(2L, 3L)), "")
				.isValid();
	}
	
	@Test
	public void idInList() throws Exception {
		validator
				.ifValid()
				.idInList(1L, Arrays.asList(new User(), new User()), "")
				.isValid();
	}
	
	@Test
	public void unique() throws Exception {
		validator
				.ifValid()
				.unique(1L, "")
				.isValid();
	}
	
	@Test
	public void exists() throws Exception {
		validator
				.ifValid()
				.exists(1L, () -> false, "")
				.isValid();
	}
	
	@Test
	public void listSize() throws Exception {
		validator
				.ifValid()
				.listSize(1, Arrays.asList(1, 2), "", "")
				.isValid();
	}
	
	@Test
	public void notLessMessage() throws Exception {
		validator
				.ifValid()
				.notLess(BigDecimal.ZERO, BigDecimal.ONE, "")
				.isValid();
		
	}
	
	@Test
	public void notLessFormat() throws Exception {
		validator
				.ifValid()
				.notLess(BigDecimal.ZERO, BigDecimal.ONE, "", "")
				.isValid();
	}
	
	@Test
	public void notGreaterMessage() throws Exception {
		validator
				.ifValid()
				.notGreater(BigDecimal.ONE, BigDecimal.ZERO, "")
				.isValid();
	}
	
	@Test
	public void notGreaterFormat() throws Exception {
		validator
				.ifValid()
				.notGreater(BigDecimal.ONE, BigDecimal.ZERO, "", "")
				.isValid();
	}
}
