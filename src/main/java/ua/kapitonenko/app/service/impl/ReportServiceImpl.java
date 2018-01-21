package ua.kapitonenko.app.service.impl;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.records.ZReport;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.ReportType;
import ua.kapitonenko.app.service.ReceiptService;
import ua.kapitonenko.app.service.ReportService;
import ua.kapitonenko.app.service.SettingsService;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl extends BaseService implements ReportService {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	ReportServiceImpl() {
	}
	
	@Override
	public boolean createZReport(Report report) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ZReportDAO reportDAO = getDaoFactory().getZReportDAO(connection.open());
			
			ZReport zReport = new ZReport(null, report.getCashboxId(), report.getCashBalance(), report.getUserId());
			
			if (reportDAO.insert(zReport)) {
				ZReport created = reportDAO.findOne(zReport.getId());
				report.setRecord(created);
				return true;
			}
			return false;
		}
	}
	
	@Override
	public long getCount() {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ZReportDAO reportDAO = getDaoFactory().getZReportDAO(connection.open());
			return reportDAO.getCount();
		}
	}
	
	@Override
	public List<Report> getReportList(int offset, int limit) {
		List<Report> reportList = new ArrayList<>();
		
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			ZReportDAO reportDAO = getDaoFactory().getZReportDAO(connection.open());
			List<ZReport> list = reportDAO.findAllByQuery("ORDER BY " + ZReportsTable.ID +
					                                              " DESC LIMIT ? OFFSET ?", ps -> {
				ps.setInt(1, limit);
				ps.setInt(2, offset);
			});
			
			list.forEach(record -> {
				Report report = getModelFactory().createReport(record.getCreatedBy());
				report.setRecord(record);
				setReferences(report);
				reportList.add(report);
			});
			return reportList;
		}
	}
	
	@Override
	public void create(Report report) {
		if (report.getType() == ReportType.Z_REPORT) {
			createZReport(report);
		}
		
		setReferences(report);
	}
	
	private void setReferences(Report report) {
		SettingsService settingsService = getServiceFactory().getSettingsService();
		ReceiptService receiptService = getServiceFactory().getReceiptService();
		
		List<Receipt> receipts;
		if (report.getRecord() != null) {
			report.setCashbox(settingsService.findCashbox(report.getRecord().getCashboxId()));
			receipts = receiptService.getReceiptList(report.getRecord().getId(), report.getRecord().getCashboxId());
		} else {
			report.setCashbox(settingsService.findCashbox(report.getCashboxId()));
			receipts = receiptService.getReceiptList(report.getCashboxId());
		}
		
		report.initSummary(receipts, settingsService.getTaxCatList(), settingsService.getPaymentTypes());
	}
	
}
