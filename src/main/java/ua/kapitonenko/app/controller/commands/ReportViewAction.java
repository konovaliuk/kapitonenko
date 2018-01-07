package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.records.PaymentType;
import ua.kapitonenko.app.domain.records.TaxCategory;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.ReportService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReportViewAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportViewAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReportService reportService = Application.getServiceFactory().getReportService();
	private ReceiptService receiptService = Application.getServiceFactory().getReceiptService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		Report report = (Report) request.getSession().get(Keys.REPORT);
		
		if (!report.isInitialized()) {
			List<Receipt> sales = receiptService.getSales(report.getCashbox().getId());
			LOGGER.debug(sales);
			List<Receipt> refunds = receiptService.getRefunds(report.getCashbox().getId());
			List<TaxCategory> taxCats = settingsService.getTaxCatList();
			List<PaymentType> paymentTypes = settingsService.getPaymentTypes();
			
			report.initSummary(sales, refunds, taxCats, paymentTypes);
		}
		
		return request.forward(Pages.REPORT_VIEW, Actions.REPORT_VIEW);
	}
}
