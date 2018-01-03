package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.exceptions.MethodNotAllowedException;
import ua.kapitonenko.exceptions.NotFoundException;
import ua.kapitonenko.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;

public class ReceiptCancelAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReceiptCreateAction.class);
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		LOGGER.debug("Cancel action");
		if (!request.isPost()) {
			throw new MethodNotAllowedException("POST");
		}
		
		String id = request.getParameter("id");
		Long receiptId = ValidationBuilder.parseId(id);
		
		LOGGER.debug(receiptId);
		
		if (receiptId == null || !receiptService.cancel(receiptId)) {
			
			throw new NotFoundException(request.getUri());
		}
		
		return request.goBack();
	}
}
