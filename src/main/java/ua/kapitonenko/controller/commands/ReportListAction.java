package ua.kapitonenko.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.config.Application;
import ua.kapitonenko.config.keys.Pages;
import ua.kapitonenko.config.keys.Routes;
import ua.kapitonenko.controller.helpers.RequestWrapper;
import ua.kapitonenko.controller.helpers.ResponseParams;
import ua.kapitonenko.service.ReportService;

import javax.servlet.ServletException;
import java.io.IOException;

public class ReportListAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportCreateAction.class);
	
	private ReportService reportService = Application.getServiceFactory().getReportService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		return request.forward(Pages.REPORT_LIST, Routes.REPORTS);
	}
}
