package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.keys.Pages;
import ua.kapitonenko.controller.keys.Routes;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.kapitonenko.controller.keys.Keys.*;

public class ReceiptListAction implements ActionCommand {
	public static final int RECORDS_PER_PAGE = 2;
	private static final Logger LOGGER = Logger.getLogger(ReceiptListAction.class);
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		int page = 1;
		
		if (request.getParameter("page") != null)
			page = Integer.parseInt(request.getParameter("page"));
		int offset = (page - 1) * RECORDS_PER_PAGE;
		
		List<ReceiptCalculator> list = receiptService.getReceiptList(offset, RECORDS_PER_PAGE, null);
		
		int noOfRecords = receiptService.getReceiptsCount();
		int noOfPages = (int) Math.ceil((double) noOfRecords / RECORDS_PER_PAGE);
		
		request.setAttribute(CUR_PAGE, page);
		request.setAttribute(NO_PAGES, noOfPages);
		request.setAttribute(RECEIPTS, list);
		return request.forward(Pages.RECEIPT_LIST, Routes.RECEIPTS);
	}
}
