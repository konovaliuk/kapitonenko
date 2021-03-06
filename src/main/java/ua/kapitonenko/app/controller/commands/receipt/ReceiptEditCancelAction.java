package ua.kapitonenko.app.controller.commands.receipt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.service.ReceiptService;

import java.lang.invoke.MethodHandles;

/**
 * Concrete implementation of {@code ActionCommand} used by {@code ReceiptEditAction}.
 * Cancels current {@link Receipt}.
 */
public class ReceiptEditCancelAction extends ReceiptEditAction {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	/**
	 * Cancels current receipt.
	 * Returns URI of receipt list on success.
	 * Returns null on failure in order to redirect to previous action.
	 */
	@Override
	protected String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator) {
		if (receiptService.update(receipt)) {
			request.getSession().remove(Keys.RECEIPT);
			request.getSession().remove(Keys.PAYMENT_TYPES);
			logger.info("{} cancelled", receipt);
			return Actions.RECEIPTS;
		}
		logger.error("Failed to cancel receipt: {}", receipt);
		return null;
	}
}
