package ua.kapitonenko.app.controller.commands.receipt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.persistence.records.Cashbox;
import ua.kapitonenko.app.persistence.records.User;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

/**
 * Implementation of {@code ActionCommand}.
 * Creates new Fiscal receipt.
 * Receives requests from receipt form, redirects request to other actions based on received command param.
 */
public class ReceiptCreateAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	/**
	 * Create new receipt only if request is POST.
	 * Saves receipt in session.
	 * Changes the payment type of receipt.
	 * Throws {@link MethodNotAllowedException} if request is not POST.
	 * Returns the URI of receipt form view or redirects to receipt processing actions.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		Receipt receipt = getReceipt(request);
		
		if (request.isPost()) {
			setPayment(request, receipt);
			
			String command = getCommand(request);
			
			switch (command) {
				case Keys.ADD:
					return request.forward(Actions.RECEIPT_EDIT_ADD, Actions.RECEIPT_CREATE);
				case Keys.UPDATE:
					return request.forward(Actions.RECEIPT_EDIT_UPDATE, Actions.RECEIPT_CREATE);
				case Keys.DELETE:
					return request.forward(Actions.RECEIPT_EDIT_DELETE, Actions.RECEIPT_CREATE);
				case Keys.SAVE:
					return request.forward(Actions.RECEIPT_EDIT_SAVE, Actions.RECEIPT_CREATE);
				case Keys.CANCEL:
					return request.forward(Actions.RECEIPT_EDIT_CANCEL, Actions.RECEIPT_CREATE);
				default:
					return request.redirect(Actions.RECEIPT_CREATE);
			}
		}
		return request.forward(Pages.RECEIPT_FORM, Actions.RECEIPT_CREATE);
	}
	
	private void setPayment(RequestWrapper request, Receipt receipt) {
		String payment = request.getParameter(Keys.PAYMENT);
		Long paymentId = ValidationBuilder.parseId(payment);
		
		if (paymentId != null) {
			receipt.setPaymentType(settingsService.findPaymentType(paymentId));
		}
	}
	
	private String getCommand(RequestWrapper request) {
		String[] buttons = request.getParams().get(Keys.BUTTON);
		if (buttons != null) {
			return Stream.of(buttons).filter(StringUtils::isNotEmpty).findFirst().orElse("");
		}
		return "";
	}
	
	private Receipt getReceipt(RequestWrapper request) {
		
		Long localeId = request.getSession().getLocaleId();
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		
		if (receipt == null) {
			if (!request.isPost()) {
				throw new MethodNotAllowedException();
			}
			
			Cashbox cashbox = (Cashbox) request.getSession().get(Keys.CASHBOX);
			User user = request.getSession().getUser();
			
			receipt = Application.getModelFactory().createReceipt(cashbox.getId(),
					Application.Ids.PAYMENT_TYPE_UNDEFINED.getValue(),
					Application.Ids.RECEIPT_TYPE_FISCAL.getValue(),
					true, user.getId());
			
			boolean created = receiptService.create(receipt);
			
			if (created) {
				logger.info("New receipt created: {}", receipt);
			}
			request.getSession().set(Keys.RECEIPT, receipt);
			request.getSession().set(Keys.PAYMENT_TYPES, settingsService.getPaymentTypes());
			request.setAttribute(Keys.NEW_PRODUCT, Application.getModelFactory().createProduct());
		}
		
		receipt.setLocalId(localeId);
		return receipt;
	}
}
