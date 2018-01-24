package ua.kapitonenko.app.controller.commands.receipt;

import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;

/**
 * Concrete implementation of {@code ActionCommand} used by {@code ReceiptEditAction}.
 * Removes products from {@link Receipt}.
 */
public class ReceiptEditDeleteAction extends ReceiptEditAction {
	
	/**
	 * Removes products from {@code Receipt}.
	 * Returns null in order to redirect to previous action.
	 */
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		String toDelete = request.getParameter(Keys.PRODUCT_ID);
		Long deleteId = ValidationBuilder.parseId(toDelete);
		if (deleteId != null) {
			receipt.remove(deleteId);
		}
		return null;
	}
}
