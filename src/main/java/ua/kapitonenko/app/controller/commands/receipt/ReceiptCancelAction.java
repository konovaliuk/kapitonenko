package ua.kapitonenko.app.controller.commands.receipt;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.exceptions.MethodNotAllowedException;
import ua.kapitonenko.app.exceptions.NotFoundException;
import ua.kapitonenko.app.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class ReceiptCancelAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		if (!request.isPost()) {
			throw new MethodNotAllowedException();
		}
		
		String id = request.getParameter("id");
		Long receiptId = ValidationBuilder.parseId(id);
		
		if (receiptId == null || !receiptService.cancel(receiptId)) {
			throw new NotFoundException();
		}
		logger.info("Receipt id={} cancelled", receiptId);
		return request.goBack();
	}
}
