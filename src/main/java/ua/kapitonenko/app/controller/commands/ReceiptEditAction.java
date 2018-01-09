package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import javax.servlet.ServletException;
import java.io.IOException;

public abstract class ReceiptEditAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReceiptEditAction.class);
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		Long localeId = request.getSession().getLocaleId();
		receipt.setLocalId(localeId);
		
		ValidationBuilder validator = new ValidationBuilder(request.getMessageManager(), request.getAlert());
		
		String action = process(receipt, request, validator);
		
		if (!validator.isValid()) {
			request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
		}
		
		return (action == null) ? request.goBack() : request.redirect(action);
	}
	
	protected abstract String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator);
}
