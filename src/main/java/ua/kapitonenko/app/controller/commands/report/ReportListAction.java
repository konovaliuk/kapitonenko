package ua.kapitonenko.app.controller.commands.report;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.PaginationHelper;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.service.ReportService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class ReportListAction implements ActionCommand {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
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
