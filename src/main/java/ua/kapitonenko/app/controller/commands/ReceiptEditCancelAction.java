package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.service.ReceiptService;

public class ReceiptEditCancelAction extends ReceiptEditAction {
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		if (receiptService.update(receipt)) {
			request.getSession().remove(Keys.RECEIPT);
			request.getSession().remove(Keys.PAYMENT_TYPES);
			return Actions.RECEIPTS;
		}
		return null;
	}
}
