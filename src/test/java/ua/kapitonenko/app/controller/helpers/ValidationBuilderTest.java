package ua.kapitonenko.app.controller.helpers;

import org.junit.Test;
import ua.kapitonenko.app.config.keys.Keys;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValidationBuilderTest {
	MessageProvider messageProvider = MessageProvider.get(new Locale("en_US"));
	AlertContainer alertContainer = new AlertContainer();
	
	@Test
	public void notLessWithFormatNegativeCompareZeroShouldBeInvalid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(new BigDecimal(-1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(false));
	}
	
	@Test
	public void notLessWithFormatZeroCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(BigDecimal.ZERO, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notLessWithFormatPositiveCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(new BigDecimal(1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notLessWithMessageNegativeCompareZeroShouldBeInvalid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(new BigDecimal(-1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(false));
	}
	
	@Test
	public void notLessWithMessageZeroCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(BigDecimal.ZERO, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notLessWithMessagePositiveCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notLess(new BigDecimal(1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notGreaterWithFormatNegativeCompareZeroShouldBeInvalid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(new BigDecimal(-1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notGreaterWithFormatZeroCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(BigDecimal.ZERO, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notGreaterWithFormatPositiveCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(new BigDecimal(1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO, Keys.PRODUCT_QUANTITY);
		assertThat(validator.isValid(), is(false));
	}
	
	@Test
	public void notGreaterWithMessageNegativeCompareZeroShouldBeInvalid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(new BigDecimal(-1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notGreaterWithMessageZeroCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(BigDecimal.ZERO, BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(true));
	}
	
	@Test
	public void notGreaterWithMessagePositiveCompareZeroShouldBeValid() throws Exception {
		ValidationBuilder validator = new ValidationBuilder(messageProvider, alertContainer);
		validator.notGreater(new BigDecimal(1), BigDecimal.ZERO, Keys.ERROR_LESS_ZERO);
		assertThat(validator.isValid(), is(false));
	}
}