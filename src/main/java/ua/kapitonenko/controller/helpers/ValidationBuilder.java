package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.Model;
import ua.kapitonenko.domain.entities.BaseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationBuilder {
	private static final Logger LOGGER = Logger.getLogger(ValidationBuilder.class);
	
	private boolean valid = true;
	private boolean allValid = true;
	private String labelAllValid;
	private MessageProvider messageManager;
	private AlertContainer alertContainer;
	
	public ValidationBuilder(MessageProvider messageManager, AlertContainer alertContainer) {
		this.messageManager = messageManager;
		this.alertContainer = alertContainer;
	}
	
	public static Long parseId(String id) {
		LongValidator longValidator = LongValidator.getInstance();
		return longValidator.validate(id);
	}
	
	public BigDecimal parseDecimal(String value, int precision, String label) {
		BigDecimalValidator validator = BigDecimalValidator.getInstance();
		BigDecimal decimal = validator.validate(value, messageManager.getLocale());
		if (decimal == null) {
			decimal = validator.validate(value, new java.util.Locale(Application.getParam(Application.DEFAULT_LOCALE)));
		}
		
		if (decimal != null) {
			BigDecimal rounded = decimal.setScale(precision, BigDecimal.ROUND_CEILING);
			if (decimal.compareTo(rounded) < 0) {
				alertContainer.addMessage(messageManager.decimalValidMessage(label, precision));
				valid = false;
			}
		}
		
		return decimal;
	}
	
	public ValidationBuilder required(String value, String label) {
		if (StringUtils.isEmpty(value)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder required(Object value, String label) {
		if (value == null) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder required(List<? extends Model> list, String label) {
		if (list == null || list.isEmpty()) {
			alertContainer.addMessage(messageManager.getProperty(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder required(String[] values, String label) {
		if (values == null || Stream.of(values).anyMatch(StringUtils::isEmpty)) {
			alertContainer.addMessage(messageManager.notEmptyLanguagesMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder requiredAll(Object value, String label) {
		if (value == null) {
			allValid = false;
			valid = false;
			labelAllValid = label;
		}
		return this;
	}
	
	public ValidationBuilder requiredOne(Long first, String firstLabel, String second, String secondLabel) {
		if (first == null && StringUtils.isEmpty(second)) {
			alertContainer.addMessage(messageManager.notEmptyOneMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder identical(String first, String firstLabel, String second, String secondLabel) {
		if (!first.equals(second)) {
			alertContainer.addMessage(messageManager.notEqualsMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder id(String id) {
		return this;
	}
	
	public ValidationBuilder idInSet(Long id, Set<Long> set, String label) {
		if (id == null || !set.contains(id)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder idInList(Long id, List<? extends BaseEntity> list, String label) {
		Set<Long> set = list.stream().map(BaseEntity::getId).collect(Collectors.toSet());
		return idInSet(id, set, label);
	}
	
	public ValidationBuilder unique(Model model, String username) {
		if (model != null) {
			alertContainer.addMessage(messageManager.notUniqueMessage(username));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder exists(Long id, Supplier<Boolean> supplier, String label) {
		if (id == null || supplier.get()) {
			alertContainer.addMessage(messageManager.notExists(label));
			valid = false;
		}
		return this;
	}
	
	public boolean isValid() {
		if (!allValid) {
			alertContainer.addMessage(messageManager.notEmptyAnyMessage(labelAllValid));
		}
		if (!valid) {
			alertContainer.setMessageType(Keys.ALERT_CLASS_DANGER);
		}
		return valid;
	}
}
