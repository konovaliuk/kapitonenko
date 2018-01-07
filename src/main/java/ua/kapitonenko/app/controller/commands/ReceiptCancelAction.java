package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;

public class ReceiptCancelAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReceiptCreateAction.class);
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		String id = request.getParameter("id");
		Long receiptId = ValidationBuilder.parseId(id);
		
		if (receiptId == null || !receiptService.cancel(receiptId)) {
			throw new NotFoundException(request.getUri());
		}
		
		return request.goBack();
	}
}
