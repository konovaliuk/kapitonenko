package ua.kapitonenko.app.controller.commands;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.PaymentType;
import ua.kapitonenko.app.service.ReceiptService;

import java.util.List;

public class ReceiptEditSaveAction extends ReceiptEditAction {
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		validator
				.idInList(receipt.getRecord().getPaymentTypeId(),
						(List<PaymentType>) request.getSession().get(Keys.PAYMENT_TYPES),
						Keys.PAYMENT)
				.required(receipt.getProducts(), Keys.ERROR_PRODUCT_LIST_NOT_EMPTY);
		
		if (validator.isValid()) {
			receipt.getRecord().setCancelled(false);
			if (receiptService.update(receipt)) {
				request.getSession().remove(Keys.RECEIPT);
				
				return Actions.RECEIPTS;
			}
		}
		return null;
	}
}
