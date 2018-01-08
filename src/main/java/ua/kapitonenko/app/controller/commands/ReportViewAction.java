package ua.kapitonenko.app.controller.commands;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.exceptions.NotFoundException;

import javax.servlet.ServletException;
import java.io.IOException;

public class ReportViewAction implements ActionCommand {
	private static final Logger LOGGER = Logger.getLogger(ReportViewAction.class);
	
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		Report report = (Report) request.getSession().get(Keys.REPORT);
		
		if (report == null || !report.getUserId().equals(request.getSession().getUserId())) {
			throw new NotFoundException("");
		}
		
		if (request.isPost()) {
			request.getSession().remove(Keys.REPORT);
			return request.redirect(Actions.REPORTS);
		}
		
		return request.forward(Pages.REPORT_VIEW, Actions.REPORT_VIEW);
	}
}
