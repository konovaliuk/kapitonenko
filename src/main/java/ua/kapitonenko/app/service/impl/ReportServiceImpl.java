package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.CashboxDAO;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.records.ZReport;
import ua.kapitonenko.app.service.ReportService;

import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {
	private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class);
	
	@Override
	public boolean createZReport(Report report) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection.open());
			
			ZReport zReport = new ZReport(null, report.getCashbox().getId(), report.getCashBalance(), report.getUserId());
			LOGGER.debug("new report " + zReport);
			
			if (reportDAO.insert(zReport)) {
				ZReport created = reportDAO.findOne(zReport.getId());
				LOGGER.debug("inserted " + created);
				report.setRecord(created);
				connection.commit();
				return true;
			}
			return false;
		}
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = Application.getConnection()) {
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection.open());
			return reportDAO.getCount();
		}
	}
	
	@Override
	public List<Report> getReportList(int offset, int limit) {
		List<Report> reportList = new ArrayList<>();
		
		try (ConnectionWrapper connection = Application.getConnection()) {
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection.open());
			CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection.open());
			List<ZReport> list = reportDAO.findAllByQuery("ORDER BY " + ZReportsTable.ID +
					                                              " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
			});
			
			list.forEach(record -> {
				Report report = new Report(record.getCreatedBy());
				report.setRecord(record);
				report.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
				
				// TODO populate receipts list
				reportList.add(report);
			});
			LOGGER.debug(reportList);
			return reportList;
		}
	}
	
}
