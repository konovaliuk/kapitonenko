package ua.kapitonenko.app.controller.helpers;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.LongValidator;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.dao.records.BaseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationBuilder {
	
	private boolean valid = true;
	private boolean allValid = true;
	private String labelAllValid;
	private MessageProvider messageManager;
	private AlertContainer alertContainer;
	private boolean skip;
	
	public ValidationBuilder(MessageProvider messageManager, AlertContainer alertContainer) {
		this.messageManager = messageManager;
		this.alertContainer = alertContainer;
	}
	
	public static Long parseId(String id) {
		LongValidator longValidator = LongValidator.getInstance();
		Long value = longValidator.validate(id);
		return (value == null || value <= 0) ? null : value;
	}
	
	public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String enumName) {
		if (!EnumUtils.isValidEnum(enumClass, enumName)) {
			return null;
		}
		return EnumUtils.getEnum(enumClass,
				enumName.toUpperCase());
	}
	
	
	public BigDecimal parseDecimal(String value, int precision, String label) {
		BigDecimalValidator validator = BigDecimalValidator.getInstance();
		BigDecimal decimal = validator.validate(value, messageManager.getLocale());
		if (decimal == null) {
			decimal = validator.validate(value, new Locale(Application.Params.DEFAULT_LOCALE.getValue()));
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
	
	public ValidationBuilder ifValid() {
		if (!valid) {
			skip = true;
		}
		return this;
	}
	
	public ValidationBuilder required(String value, String label) {
		if (skip) {
			return this;
		}
		
		if (StringUtils.isEmpty(value)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder required(Object value, String label) {
		if (skip) {
			return this;
		}
		if (value == null) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder required(List list, String label) {
		if (skip) {
			return this;
		}
		
		if (list == null || list.isEmpty()) {
			alertContainer.addMessage(messageManager.getProperty(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder requiredAllLang(String[] values, String label) {
		if (skip) {
			return this;
		}
		
		if (values == null || Stream.of(values).anyMatch(StringUtils::isEmpty)) {
			alertContainer.addMessage(messageManager.notEmptyLanguagesMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder requiredEach(Object value, String label) {
		if (skip) {
			return this;
		}
		
		if (value == null) {
			allValid = false;
			valid = false;
			labelAllValid = label;
		}
		return this;
	}
	
	public ValidationBuilder requiredOne(Long first, String firstLabel, String second, String secondLabel) {
		if (skip) {
			return this;
		}
		
		if (first == null && StringUtils.isEmpty(second)) {
			alertContainer.addMessage(messageManager.notEmptyOneMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder identical(String first, String firstLabel, String second, String secondLabel) {
		if (skip) {
			return this;
		}
		
		if (!first.equals(second)) {
			alertContainer.addMessage(messageManager.notEqualsMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	
	public ValidationBuilder idInSet(Long id, Set<Long> set, String label) {
		if (skip) {
			return this;
		}
		
		if (id == null || !set.contains(id)) {
			alertContainer.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder idInList(Long id, List<? extends BaseEntity> list, String label) {
		if (skip) {
			return this;
		}
		
		Set<Long> set = list.stream().map(BaseEntity::getId).collect(Collectors.toSet());
		return idInSet(id, set, label);
	}
	
	public ValidationBuilder unique(Object object, String label) {
		if (skip) {
			return this;
		}
		
		if (object != null) {
			alertContainer.addMessage(messageManager.notUniqueMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder exists(Long id, Supplier<Boolean> supplier, String label) {
		if (skip) {
			return this;
		}
		
		if (id == null || !supplier.get()) {
			alertContainer.addMessage(messageManager.notExists(label));
			valid = false;
		}
		return this;
	}
	
	
	public ValidationBuilder listSize(int size, List list, String messageLess, String messageMore) {
		if (skip) {
			return this;
		}
		
		if (list == null || list.isEmpty()) {
			alertContainer.addMessage(
					messageManager.getProperty(messageLess));
			valid = false;
		} else if (list.size() > size) {
			alertContainer.addMessage(messageManager.getProperty(messageMore));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder notLess(BigDecimal value, BigDecimal base, String message) {
		if (skip) {
			return this;
		}
		
		if (value.compareTo(base) < 0) {
			alertContainer.addMessage(messageManager.getProperty(message));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder notLess(BigDecimal value, BigDecimal base, String format, String attr) {
		if (skip) {
			return this;
		}
		
		if (value.compareTo(base) < 0) {
			String message = messageManager.concat(format, attr);
			alertContainer.addMessage(message);
			valid = false;
		}
		return this;
	}
	
	
	public ValidationBuilder notGreater(BigDecimal value, BigDecimal base, String message) {
		if (skip) {
			return this;
		}
		
		if (value.compareTo(base) > 0) {
			alertContainer.addMessage(messageManager.getProperty(message));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder notGreater(BigDecimal value, BigDecimal base, String format, String attr) {
		if (skip) {
			return this;
		}
		
		if (value.compareTo(base) > 0) {
			String message = messageManager.concat(format, attr);
			alertContainer.addMessage(message);
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
