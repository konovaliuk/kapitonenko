package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.CashboxDAO;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.records.ZReport;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.ReportService;
import ua.kapitonenko.app.service.ServiceFactory;
import ua.kapitonenko.app.service.SettingsService;

import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {
	private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class);
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	private ServiceFactory serviceFactory;
	
	private ServiceFactory getServiceFactory() {
		if (serviceFactory == null) {
			serviceFactory = Application.getServiceFactory();
		}
		return serviceFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public boolean createZReport(Report report) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ZReportDAO reportDAO = daoFactory.getZReportDAO(connection.open());
			
			ZReport zReport = new ZReport(null, report.getCashbox().getId(), report.getCashBalance(), report.getUserId());
			LOGGER.debug("new report " + zReport);
			
			if (reportDAO.insert(zReport)) {
				ZReport created = reportDAO.findOne(zReport.getId());
				LOGGER.debug("inserted " + created);
				report.setRecord(created);
				return true;
			}
			return false;
		}
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ZReportDAO reportDAO = daoFactory.getZReportDAO(connection.open());
			return reportDAO.getCount();
		}
	}
	
	@Override
	public List<Report> getReportList(int offset, int limit) {
		List<Report> reportList = new ArrayList<>();
		ReceiptService receiptService = getServiceFactory().getReceiptService();
		SettingsService settingsService = getServiceFactory().getSettingsService();
		
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			ZReportDAO reportDAO = daoFactory.getZReportDAO(connection.open());
			CashboxDAO cashboxDAO = daoFactory.getCashboxDao(connection.open());
			List<ZReport> list = reportDAO.findAllByQuery("ORDER BY " + ZReportsTable.ID +
					                                              " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
			});
			
			list.forEach(record -> {
				Report report = new Report(record.getCreatedBy());
				report.setRecord(record);
				report.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
				
				List<Receipt> receipts = receiptService.getReceiptList(record.getId(), record.getCashboxId());
				report.initSummary(receipts, settingsService.getTaxCatList(), settingsService.getPaymentTypes());
				reportList.add(report);
			});
			LOGGER.debug(reportList);
			return reportList;
		}
	}
	
}
