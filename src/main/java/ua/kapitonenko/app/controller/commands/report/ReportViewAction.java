package ua.kapitonenko.app.controller.commands.report;

import ua.kapitonenko.app.config.keys.Actions;
import ua.kapitonenko.app.config.keys.Keys;
import ua.kapitonenko.app.config.keys.Pages;
import ua.kapitonenko.app.controller.commands.ActionCommand;
import ua.kapitonenko.app.controller.helpers.RequestWrapper;
import ua.kapitonenko.app.controller.helpers.ResponseParams;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.exceptions.NotFoundException;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Implementation of {@code ActionCommand}.
 * Gets the {@link Report} from session and displays it in report view.
 */
public class ReportViewAction implements ActionCommand {
	
	/**
	 * Gets the {@link Report} from session and displays it in report view.
	 * Returns the URI of report view.
	 */
	@Override
	public ResponseParams execute(RequestWrapper request) throws ServletException, IOException {
		
		Report report = (Report) request.getSession().get(Keys.REPORT);
		
		if (report == null || !report.getUserId().equals(request.getSession().getUserId())) {
			throw new NotFoundException();
		}
		
		if (request.isPost()) {
			request.getSession().remove(Keys.REPORT);
			return request.redirect(Actions.REPORTS);
		}
		
		return request.forward(Pages.REPORT_VIEW, Actions.REPORT_VIEW);
	}
}
