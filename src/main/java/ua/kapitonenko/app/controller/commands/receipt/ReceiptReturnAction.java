package ua.kapitonenko.app.controller.commands.receipt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

/**
 * Implementation of {@code ActionCommand}.
 * Creates new Return receipt.
 * Receives requests from receipt form, redirects request to other actions based on received command param.
 */
public class ReceiptReturnAction implements ActionCommand {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
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
		
		initReceipt(request);
		
		if (request.isPost()) {
			String command = getCommand(request);
			
			switch (command) {
				case Keys.UPDATE:
					return request.forward(Actions.RECEIPT_EDIT_UPDATE, Actions.RECEIPT_RETURN);
				case Keys.DELETE:
					return request.forward(Actions.RECEIPT_EDIT_DELETE, Actions.RECEIPT_RETURN);
				case Keys.SAVE:
					return request.forward(Actions.RECEIPT_EDIT_SAVE, Actions.RECEIPT_RETURN);
				case Keys.CANCEL:
					return request.forward(Actions.RECEIPT_EDIT_CANCEL, Actions.RECEIPT_RETURN);
				default:
					return request.redirect(Actions.RECEIPT_RETURN);
			}
		}
		
		request.setAttribute(Keys.NEW_PRODUCT, Application.getModelFactory().createProduct());
		return request.forward(Pages.RECEIPT_FORM, Actions.RECEIPT_RETURN);
	}
	
	private String getCommand(RequestWrapper request) {
		String[] buttons = request.getParams().get(Keys.BUTTON);
		if (buttons != null) {
			return Stream.of(buttons).filter(StringUtils::isNotEmpty).findFirst().orElse("");
		}
		return "";
	}
	
	private void initReceipt(RequestWrapper request) {
		
		Long localeId = request.getSession().getLocaleId();
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		
		if (receipt == null) {
			if (!request.isPost()) {
				throw new MethodNotAllowedException();
			}
			
			String id = request.getParameter("id");
			Long receiptId = ValidationBuilder.parseId(id);
			
			receipt = receiptService.findOne(receiptId);
			boolean created = receiptService.createReturn(receipt);
			
			if (created) {
				logger.info("New return receipt created: {}", receipt);
			}
			
			request.getSession().set(Keys.RECEIPT, receipt);
			request.getSession().set(Keys.PAYMENT_TYPES, settingsService.getPaymentTypes());
		}
		
		receipt.setLocalId(localeId);
		
	}
}
