package ua.kapitonenko.app.controller.commands.receipt;

import ua.kapitonenko.app.config.AccessControl;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.PaginationHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.persistence.records.Cashbox;
import ua.kapitonenko.app.service.ReceiptService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of {@code ActionCommand}.
 * Gets the list of {@link Receipt} from {@link ReceiptService} and displays them in list view.
 */
public class ReceiptListAction implements ActionCommand {
	
	
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	/**
	 * For users with the role Senior Cashier displays list of all receipts with pagination.
	 * For other roles displays only current shift receipts.
	 * Returns the URI of list view.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		long noOfRecords = receiptService.getCount();
		PaginationHelper pager = new PaginationHelper(request, noOfRecords);
		
		List<Receipt> list;
		boolean userIsSenior = AccessControl.allowed(request.getSession().getUser().getUserRoleId(), Actions.REPORT_CREATE);
		if (userIsSenior) {
			list = receiptService.getReceiptList(pager.getOffset(), pager.getRecordsPerPage());
		} else {
			Long cashboxId = ((Cashbox) request.getSession().get(Keys.CASHBOX)).getId();
			list = receiptService.getReceiptList(cashboxId);
		}
		
		request.setAttribute(Keys.RECEIPTS, list);
		return request.forward(Pages.RECEIPT_LIST, Actions.RECEIPTS);
	}
}
