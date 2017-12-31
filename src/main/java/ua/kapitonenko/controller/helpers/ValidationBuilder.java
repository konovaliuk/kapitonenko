package ua.kapitonenko.controller.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.LongValidator;
import ua.kapitonenko.controller.keys.Keys;
import ua.kapitonenko.domain.Model;

import java.util.Set;
import java.util.function.Supplier;

public class ValidationBuilder {
	MessageProvider messageManager;
	ViewHelper viewHelper;
	boolean valid = true;
	
	public ValidationBuilder(MessageProvider messageManager, ViewHelper viewHelper) {
		this.messageManager = messageManager;
		this.viewHelper = viewHelper;
	}
	
	public static Long parseId(String id) {
		LongValidator longValidator = LongValidator.getInstance();
		return longValidator.validate(id);
	}
	
	public ValidationBuilder required(String value, String label) {
		if (StringUtils.isEmpty(value)) {
			viewHelper.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder identical(String first, String firstLabel, String second, String secondLabel) {
		if (!first.equals(second)) {
			viewHelper.addMessage(messageManager.notEqualsMessage(firstLabel, secondLabel));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder id(String id) {
		return this;
	}
	
	public ValidationBuilder idInSet(Long id, Set<Long> set, String label) {
		if (id == null || !set.contains(id)) {
			viewHelper.addMessage(messageManager.notEmptyMessage(label));
			valid = false;
		}
		return this;
	}
	
	public ValidationBuilder unique(Model model, String username) {
		if (model != null) {
			viewHelper.addMessage(messageManager.notUniqueMessage(username));
			valid = false;
		}
		return this;
	}
	
	public boolean isValid() {
		if (!valid) {
			viewHelper.setMessageType(Keys.ALERT_CLASS_DANGER);
		}
		return valid;
	}
	
	public ValidationBuilder exists(Long id, Supplier<Boolean> supplier, String label) {
		if (id == null || supplier.get()) {
			viewHelper.addMessage(messageManager.notExists(label));
			valid = false;
		}
		return this;
	}
}
