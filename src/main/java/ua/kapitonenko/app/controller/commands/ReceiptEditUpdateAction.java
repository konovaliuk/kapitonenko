package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;

import java.math.BigDecimal;

public class ReceiptEditUpdateAction extends ReceiptEditAction {
	
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		String[] updatedQuantities = request.getParams().get(Keys.PRODUCT_QUANTITY);
		for (int i = 0; i < updatedQuantities.length; i++) {
			BigDecimal updated = validator.parseDecimal(updatedQuantities[i], 3, Keys.PRODUCT_QUANTITY);
			validator.requiredAll(updated, Keys.PRODUCT_QUANTITY);
			if (updated != null) {
				receipt.getProducts().get(i).setQuantity(updated);
			}
		}
		return null;
	}
}
