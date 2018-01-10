package ua.kapitonenko.app.controller.helpers;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ValidationBuilderStaticMethodsTest {
	
	@Test
	public void parseIdWithNullShouldReturnNull() {
		assertThat(ValidationBuilder.parseId(null), is(nullValue()));
	}
	
	@Test
	public void parseIdWithEmptyShouldReturnNull() {
		assertThat(ValidationBuilder.parseId(""), is(nullValue()));
	}
	
	@Test
	public void parseIdWithFloatShouldReturnNull() {
		assertThat(ValidationBuilder.parseId("1.5"), is(nullValue()));
	}
	
	@Test
	public void parseIdWithNegativeShouldReturnNull() {
		assertThat(ValidationBuilder.parseId("-10"), is(nullValue()));
	}
	
	@Test
	public void parseIdWithNanShouldReturnNull() {
		assertThat(ValidationBuilder.parseId("one"), is(nullValue()));
	}
	
	@Test
	public void parseIdWithZeroShouldReturnNull() {
		assertThat(ValidationBuilder.parseId("0"), is(nullValue()));
	}
	
	@Test
	public void parseIdWithOneShouldReturnLong() {
		assertThat(ValidationBuilder.parseId("1"), is(equalTo(1L)));
	}
	
	@Test
	public void parseIdWithMaxLongShouldReturnLong() {
		assertThat(ValidationBuilder.parseId("9223372036854775807"), is(equalTo(Long.MAX_VALUE)));
	}
	
	@Test
	public void parseEnumWithValidCapsShoulReturnEnum() {
		assertThat(ValidationBuilder.parseEnum(TestEnum.class, "ONE"), is(equalTo(TestEnum.ONE)));
	}
	
	@Test
	public void parseEnumWithValidLowercaseShoulReturnNull() {
		assertThat(ValidationBuilder.parseEnum(TestEnum.class, "one"), is(nullValue()));
	}
	
	@Test
	public void parseEnumWithInvalidCapsShoulReturnNull() {
		assertThat(ValidationBuilder.parseEnum(TestEnum.class, "TWO"), is(nullValue()));
	}
	
	private enum TestEnum {
		ONE
	}
}