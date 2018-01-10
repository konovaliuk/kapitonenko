package ua.kapitonenko.app.controller.helpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidationBuilderMessagesTest {
	
	private ValidationBuilder validator;
	
	@Mock
	private MessageProvider messageProvider;
	
	@Mock
	private AlertContainer alert;
	
	@Before
	public void createValidationBuilder() {
		validator = new ValidationBuilder(messageProvider, alert);
	}
	
	@Test
	public void requiredWithInvalidStringShouldAddMessage() {
		String label = "name";
		String value = null;
		when(messageProvider.notEmptyMessage(label)).thenReturn(label);
		
		validator.required(value, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredWithValidStringShouldNotAddMessage() {
		String label = "name";
		String value = "bob";
		
		validator.required(value, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void requiredWithInvalidObjectShouldAddMessage() {
		String label = "object";
		Object value = null;
		when(messageProvider.notEmptyMessage(label)).thenReturn(label);
		
		validator.required(value, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredWithValidObjectShouldNotAddMessage() {
		String label = "object";
		Object value = new Object();
		
		validator.required(value, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void requiredWithInvalidListShouldAddMessage() {
		String label = "list";
		List value = null;
		when(messageProvider.getProperty(label)).thenReturn(label);
		
		validator.required(value, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredWithValidListShouldNotAddMessage() {
		String label = "list";
		List value = Arrays.asList(1, 2);
		
		validator.required(value, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void requiredAllLangWithInvalidArrayShouldAddMessage() {
		String label = "array";
		String[] value = {""};
		when(messageProvider.notEmptyLanguagesMessage(label)).thenReturn(label);
		
		validator.requiredAllLang(value, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredAllLangWithValidArrayShouldNotAddMessage() {
		String label = "array";
		String[] value = {"1", "2"};
		
		validator.requiredAllLang(value, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void requiredOneWithInvalidArgsShouldAddMessage() {
		String label = "at least one";
		Long first = null;
		String second = null;
		when(messageProvider.notEmptyOneMessage(label, label)).thenReturn(label);
		
		validator.requiredOne(first, label, second, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredOneWithValidArgsShouldNotAddMessage() {
		String label = "at least one";
		Long first = 1L;
		String second = "name";
		
		validator.requiredOne(first, label, second, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void identicalWithInvalidArgsShouldAddMessage() {
		String label = "identical";
		String first = "pass";
		String second = "ass";
		when(messageProvider.notEqualsMessage(label, label)).thenReturn(label);
		
		validator.identical(first, label, second, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void identicalWithValidArgsShouldNotAddMessage() {
		String label = "identical";
		String first = "pass";
		String second = "pass";
		
		validator.identical(first, label, second, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void idInSetWithInvalidArgsShouldAddMessage() {
		String label = "in set";
		Long id = 5L;
		Set<Long> ids = new HashSet<>(Arrays.asList(90L, 100L));
		when(messageProvider.notEmptyMessage(label)).thenReturn(label);
		
		validator.idInSet(id, ids, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void idInSetWithValidArgsShouldNotAddMessage() {
		String label = "in set";
		Long id = 90L;
		Set<Long> ids = new HashSet<>(Arrays.asList(90L, 100L));
		
		validator.idInSet(id, ids, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void uniqueWithInvalidObjectShouldAddMessage() {
		String label = "object";
		Object value = new Object();
		when(messageProvider.notUniqueMessage(label)).thenReturn(label);
		
		validator.unique(value, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void uniqueWithValidObjectShouldNotAddMessage() {
		String label = "object";
		Object value = null;
		
		validator.unique(value, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void existsWithInvalidArgsShouldAddMessage() {
		String label = "exists";
		Long value = 10L;
		Supplier<Boolean> supplier = () -> false;
		when(messageProvider.notExists(label)).thenReturn(label);
		
		validator.exists(value, supplier, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void existsWithValidArgsShouldNotAddMessage() {
		String label = "exists";
		Long value = 3L;
		Supplier<Boolean> supplier = () -> true;
		
		validator.exists(value, supplier, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void listSizeWithEmptyListShouldAddMessage() {
		String less = "less";
		String more = "more";
		int size = 1;
		List list = Collections.emptyList();
		when(messageProvider.getProperty(less)).thenReturn(less);
		
		validator.listSize(size, list, less, more);
		
		verify(alert).addMessage(less);
	}
	
	@Test
	public void listSizeWithLargerListShouldAddMessage() {
		String less = "less";
		String more = "more";
		int size = 1;
		List list = Arrays.asList(1, 2);
		when(messageProvider.getProperty(more)).thenReturn(more);
		
		validator.listSize(size, list, less, more);
		
		verify(alert).addMessage(more);
	}
	
	@Test
	public void listSizeWithValidListShouldNotAddMessage() {
		String label = "list";
		int size = 2;
		List list = Arrays.asList(1, 2);
		
		validator.listSize(size, list, label, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void notLessWithInvalidArgsShouldAddMessage() {
		String label = "message";
		BigDecimal value = BigDecimal.ZERO;
		BigDecimal base = BigDecimal.ONE;
		when(messageProvider.getProperty(label)).thenReturn(label);
		
		validator.notLess(value, base, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void notLessWithValidArgsShouldNotAddMessage() {
		String label = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ONE;
		
		validator.notLess(value, base, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void notLessFormatWithInvalidArgsShouldAddMessage() {
		String label = "message";
		String format = "message";
		BigDecimal value = BigDecimal.ZERO;
		BigDecimal base = BigDecimal.ONE;
		when(messageProvider.concat(label, format)).thenReturn(label);
		
		validator.notLess(value, base, format, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void notLessFormatWithValidArgsShouldNotAddMessage() {
		String label = "message";
		String format = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ONE;
		
		validator.notLess(value, base, format, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void notGreaterWithInvalidArgsShouldAddMessage() {
		String label = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ZERO;
		when(messageProvider.getProperty(label)).thenReturn(label);
		
		validator.notGreater(value, base, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void notGreaterWithValidArgsShouldNotAddMessage() {
		String label = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ONE;
		
		validator.notGreater(value, base, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void notGreaterFormatWithInvalidArgsShouldAddMessage() {
		String label = "message";
		String format = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ZERO;
		when(messageProvider.concat(label, format)).thenReturn(label);
		
		validator.notGreater(value, base, format, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void notGreaterFormatWithValidArgsShouldNotAddMessage() {
		String label = "message";
		String format = "message";
		BigDecimal value = BigDecimal.ONE;
		BigDecimal base = BigDecimal.ONE;
		
		validator.notGreater(value, base, format, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void requiredEachWithInvalidArgsShouldAddMessage() {
		String label = "attr";
		Object value = null;
		when(messageProvider.notEmptyAnyMessage(label)).thenReturn(label);
		
		validator.requiredEach(value, label).isValid();
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void requiredEachWithValidArgsShouldAddMessage() {
		String label = "attr";
		Object value = new Object();
		
		validator.requiredEach(value, label).isValid();
		
		verify(alert, never()).addMessage(anyString());
	}
	
	@Test
	public void isValidOnInvalidValidatorShouldSetMessageType() {
		String label = "attr";
		Object value = null;
		
		validator.requiredEach(value, label).isValid();
		
		verify(alert).setMessageType(anyString());
	}
	
	@Test
	public void isValidOnValidValidatorShouldNotSetMessageType() {
		String label = "attr";
		Object value = "";
		
		validator.requiredEach(value, label).isValid();
		
		verify(alert, never()).setMessageType(anyString());
	}
	
	@Test
	public void parseDecimalWithInvalidArgsShouldAddMessage() {
		String value = "10.5";
		int precision = 0;
		String label = "label";
		when(messageProvider.decimalValidMessage(label, precision)).thenReturn(label);
		
		validator.parseDecimal(value, precision, label);
		
		verify(alert).addMessage(label);
	}
	
	@Test
	public void parseDecimalWithValidArgsShouldNotAddMessage() {
		String value = "10";
		int precision = 0;
		String label = "label";
		
		validator.parseDecimal(value, precision, label);
		
		verify(alert, never()).addMessage(anyString());
	}
	
	
}
