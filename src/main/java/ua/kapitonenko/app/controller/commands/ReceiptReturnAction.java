package ua.kapitonenko.app.controller.commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.stream.Stream;

public class ReceiptReturnAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(ReceiptReturnAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
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
		
		return request.forward(Pages.RECEIPT_FORM, Actions.RECEIPT_RETURN);
	}
	
	private String getCommand(RequestWrapper request) {
		String[] buttons = request.getParams().get(Keys.BUTTON);
		if (buttons != null) {
			String command = Stream.of(buttons).filter(StringUtils::isNotEmpty).findFirst().orElse("");
			LOGGER.debug("command: " + command);
			return command;
		}
		return "";
	}
	
	private void initReceipt(RequestWrapper request) {
		
		Long localeId = request.getSession().getLocaleId();
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		
		if (receipt == null) {
			String id = request.getParameter("id");
			Long receiptId = ValidationBuilder.parseId(id);
			
			receipt = receiptService.findOne(receiptId);
			receiptService.createReturn(receipt);
			
			request.getSession().set(Keys.RECEIPT, receipt);
			request.getSession().set(Keys.PAYMENT_TYPES, settingsService.getPaymentTypes());
		}
		
		receipt.setLocalId(localeId);
		
	}
}
