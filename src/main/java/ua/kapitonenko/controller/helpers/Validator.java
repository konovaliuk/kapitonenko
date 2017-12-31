package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.BaseEntity;
import ua.kapitonenko.domain.Model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Validator {
	private static final Logger LOGGER = Logger.getLogger(Validator.class);
	boolean valid = true;
	private MessageProvider messageManager;
	private AlertContainer alertContainer;
	
	public Validator(MessageProvider messageManager, AlertContainer alertContainer) {
		this.messageManager = messageManager;
		this.alertContainer = alertContainer;
	}
	
	public static Long parseId(String id) {
		LongValidator longValidator = LongValidator.getInstance();
		return longValidator.validate(id);
	}
	
	public Validator required(String value, String label) {
		if (StringUtils.isEmpty(value)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public Validator required(Object value, String label) {
		if (value == null) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public Validator required(String[] values, String label) {
		LOGGER.debug(Arrays.toString(values));
		if (values == null || Stream.of(values).anyMatch(StringUtils::isEmpty)) {
			alertContainer.addMessage(messageManager.notEmptyLanguagesMessage(label));
			valid = false;
		}
		return this;
	}
	
	public Validator identical(String first, String firstLabel, String second, String secondLabel) {
		if (!first.equals(second)) {
			alertContainer.addMessage(messageManager.notEqualsMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	public Validator id(String id) {
		return this;
	}
	
	public Validator idInSet(Long id, Set<Long> set, String label) {
		if (id == null || !set.contains(id)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public Validator idInList(Long id, List<? extends BaseEntity> list, String label) {
		Set<Long> set = list.stream().map(BaseEntity::getId).collect(Collectors.toSet());
		return idInSet(id, set, label);
	}
	
	public Validator unique(Model model, String username) {
		if (model != null) {
			alertContainer.addMessage(messageManager.notUniqueMessage(username));
			valid = false;
		}
		return this;
	}
	
	public boolean isValid() {
		if (!valid) {
			alertContainer.setMessageType(Keys.ALERT_CLASS_DANGER);
		}
		return valid;
	}
	
	public Validator exists(Long id, Supplier<Boolean> supplier, String label) {
		if (id == null || supplier.get()) {
			alertContainer.addMessage(messageManager.notExists(label));
			valid = false;
		}
		return this;
	}
	
	public BigDecimal parseDecimal(String value, int precision, String label) {
		BigDecimalValidator validator = BigDecimalValidator.getInstance();
		BigDecimal decimal = validator.validate(value, messageManager.getLocale());
		if (decimal == null){
			decimal = validator.validate(value, new java.util.Locale(Application.defaultLocale));
		}
		
		
		
		LOGGER.debug(messageManager.getLocale().toString());
		LOGGER.debug(decimal + " " + label);
		
		if (decimal != null){
			BigDecimal rounded = decimal.setScale(precision, BigDecimal.ROUND_CEILING);
			if (decimal.compareTo(rounded) < 0){
				alertContainer.addMessage(label + " must have " + precision + " decimal places or less");
				valid = false;
			}
		}
		
		return decimal;
		
	}
}
