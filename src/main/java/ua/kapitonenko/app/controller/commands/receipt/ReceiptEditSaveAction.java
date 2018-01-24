package ua.kapitonenko.app.controller.commands.receipt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.persistence.records.PaymentType;
import ua.kapitonenko.app.service.ReceiptService;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Concrete implementation of {@code ActionCommand} used by {@code ReceiptEditAction}.
 * Saves current {@link Receipt} in storage and cancels receipt creation session.
 */
public class ReceiptEditSaveAction extends ReceiptEditAction {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	/**
	 * Validates request params, saves current receipt in storage.
	 * Removes receipt from session.
	 * Returns URI of receipt list on success.
	 * Returns null on failure in order to redirect to previous action.
	 */
	@SuppressWarnings("unchecked")
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
				logger.info("{} saved", receipt);
				return Actions.RECEIPTS;
			}
		}
		logger.error("Failed to save receipt: {}", receipt);
		return null;
	}
}
