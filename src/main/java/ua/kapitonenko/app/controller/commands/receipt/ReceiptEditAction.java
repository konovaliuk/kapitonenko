package ua.kapitonenko.app.controller.commands.receipt;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public abstract class ReceiptEditAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		
		Receipt receipt = (Receipt) request.getSession().get(Keys.RECEIPT);
		Long localeId = request.getSession().getLocaleId();
		receipt.setLocalId(localeId);
		
		ValidationBuilder validator = request.getValidator();
		
		String action = process(receipt, request, validator);
		
		if (!validator.isValid()) {
			request.getSession().setFlash(request.getAlert().getMessageType(), request.getAlert().joinMessages());
			logger.warn("Receipt edit validation error: message='{}', {}", request.getAlert().joinMessages(), request.paramsToString());
		}
		
		return (action == null) ? request.goBack() : request.redirect(action);
	}
	
	protected abstract String process(Receipt receipt, RequestWrapper request, ValidationBuilder validator);
}
