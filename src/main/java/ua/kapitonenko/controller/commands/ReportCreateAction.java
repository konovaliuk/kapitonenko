package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Keys;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.controller.helpers.ValidationBuilder;
import ua.kapitonenko.domain.Report;
import ua.kapitonenko.domain.ReportType;
import ua.kapitonenko.domain.entities.Cashbox;
import ua.kapitonenko.service.SettingsService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReportCreateAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportCreateAction.class);
	
	private SettingsService settingsService = Application.getServiceFactory().getSettingsService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		ReportType[] types = ReportType.values();
		List<Cashbox> cashboxList = settingsService.getCashboxList();
		
		Report report = new Report();
		
		if (request.isPost()) {
			String cashbox = request.getParameter(Keys.REPORT_CASHBOX);
			String type = request.getParameter(Keys.REPORT_TYPE);
			
			report.setType(ReportType.valueOf(type));
			report.setCashbox(settingsService.findCashbox(ValidationBuilder.parseId(cashbox)));
			request.getSession().set(Keys.REPORT, report);
			
			return request.redirect(Routes.REPORT_VIEW);
			
		}
		
		request.setAttribute(Keys.REPORT, report);
		request.setAttribute(Keys.CASHBOX_LIST, cashboxList);
		request.setAttribute(Keys.REPORT_TYPES, types);
		
		return request.forward(Pages.REPORT_FORM, Routes.REPORT_CREATE);
	}
}
