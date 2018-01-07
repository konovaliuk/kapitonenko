package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.Report;
import ua.kapitonenko.domain.entities.PaymentType;
import ua.kapitonenko.domain.entities.TaxCategory;
import ua.kapitonenko.service.ReceiptService;
import ua.kapitonenko.service.ReportService;
import ua.kapitonenko.service.SettingsService;

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
			List<ReceiptCalculator> sales = receiptService.getSales(report.getCashbox().getId());
			LOGGER.debug(sales);
			List<ReceiptCalculator> refunds = receiptService.getRefunds(report.getCashbox().getId());
			List<TaxCategory> taxCats = settingsService.getTaxCatList();
			List<PaymentType> paymentTypes = settingsService.getPaymentTypes();
			
			report.initSummary(sales, refunds, taxCats, paymentTypes);
		}
		
		return request.forward(Pages.REPORT_VIEW, Routes.REPORT_VIEW);
	}
}
