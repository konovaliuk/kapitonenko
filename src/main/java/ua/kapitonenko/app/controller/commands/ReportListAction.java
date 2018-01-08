package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.PaginationHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.service.ReportService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class ReportListAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportCreateAction.class);
	
	private ReportService reportService = Application.getServiceFactory().getReportService();
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		long noOfRecords = reportService.getCount();
		PaginationHelper pager = new PaginationHelper(request, noOfRecords);
		
		List<Report> list = reportService.getReportList(pager.getOffset(), pager.getRecordsPerPage());
		
		request.setAttribute(Keys.REPORTS, list);
		return request.forward(Pages.REPORT_LIST, Actions.REPORTS);
		
	}
}
