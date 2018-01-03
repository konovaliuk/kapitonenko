package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.PaginationHelper;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReceiptListAction implements ActionCommand {
	
	private static final Logger LOGGER = Logger.getLogger(ReceiptListAction.class);
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		int noOfRecords = receiptService.getReceiptsCount();
		PaginationHelper pager = new PaginationHelper(request, noOfRecords);
		
		List<ReceiptCalculator> list = receiptService.getReceiptList(pager.getOffset(), pager.getRecordsPerPage(), null);
		
		request.setAttribute(Keys.RECEIPTS, list);
		return request.forward(Pages.RECEIPT_LIST, Routes.RECEIPTS);
	}
}
