package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.controller.helpers.ValidationBuilder;
import ua.kapitonenko.app.dao.records.Cashbox;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.ReportType;
import ua.kapitonenko.app.service.ReportService;
import ua.kapitonenko.app.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReportCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	private ReportService reportService = Application.getServiceFactory().getReportService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		ReportType[] types = ReportType.values();
		List<Cashbox> cashboxList = settingsService.getCashboxList();
		
		Report report = Application.getModelFactory().createReport(request.getSession().getUserId());
		
		if (request.isPost()) {
			String cashbox = request.getParameter(Keys.REPORT_CASHBOX);
			String type = request.getParameter(Keys.REPORT_TYPE);
			
			Long cashboxId = ValidationBuilder.parseId(cashbox);
			ReportType reportType = ValidationBuilder.parseEnum(ReportType.class, type);
			
			ValidationBuilder validator = request.getValidator();
			
			validator.required(cashboxId, Keys.CASHBOX)
					.required(reportType, Keys.REPORT_TYPE)
					.ifValid()
					.idInList(cashboxId, cashboxList, Keys.CASHBOX);
			
			report.setType(reportType);
			
			if (validator.isValid()) {
				report.setCashboxId(cashboxId);
				report.setType(reportType);
				
				reportService.create(report);
				request.getSession().set(Keys.REPORT, report);
				
				return request.redirect(Actions.REPORT_VIEW);
			}
		}
		
		request.setAttribute(Keys.REPORT, report);
		request.setAttribute(Keys.CASHBOX_LIST, cashboxList);
		request.setAttribute(Keys.REPORT_TYPES, types);
		
		return request.forward(Pages.REPORT_FORM, Actions.REPORT_CREATE);
	}
}
