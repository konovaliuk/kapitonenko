package ua.kapitonenko.app.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionPool;
import ua.kapitonenko.app.dao.interfaces.CashboxDAO;
import ua.kapitonenko.app.dao.interfaces.ZReportDAO;
import ua.kapitonenko.app.dao.tables.ZReportsTable;
import ua.kapitonenko.app.domain.Report;
import ua.kapitonenko.app.domain.records.ZReport;
import ua.kapitonenko.app.exceptions.DAOException;
import ua.kapitonenko.app.service.ReportService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceImpl implements ReportService {
	private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class);
	
	private static ReportServiceImpl instance = new ReportServiceImpl();
	private static ConnectionPool pool = Application.getConnectionPool();
	
	private ReportServiceImpl() {
	}
	
	public static ReportServiceImpl getInstance() {
		return instance;
	}
	
	@Override
	public boolean createZReport(Report report) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			connection.setAutoCommit(false);
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection);
			
			
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
			
		} catch (SQLException e) {
			try {
				connection.rollback();
				return false;
			} catch (SQLException e1) {
				throw new DAOException(e);
			}
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public long getCount() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection);
			return reportDAO.getCount();
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public List<Report> getReportList(int offset, int recordsPerPage) {
		List<Report> reportList = new ArrayList<>();
		Connection connection = pool.getConnection();
		try {
			ZReportDAO reportDAO = Application.getDAOFactory().getZReportDAO(connection);
			CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection);
			List<ZReport> list = reportDAO.findAllByQuery("ORDER BY ? LIMIT ? OFFSET ?", ps -> {
				ps.setString(1, ZReportsTable.ID);
				ps.setInt(3, offset);
				ps.setInt(2, recordsPerPage);
			});
			
			list.forEach(record -> {
				Report report = new Report(record.getCreatedBy());
				report.setRecord(record);
				report.setCashbox(cashboxDAO.findOne(record.getCashboxId()));
				reportList.add(report);
			});
			LOGGER.debug(reportList);
			return reportList;
		} finally {
			pool.close(connection);
		}
	}
	
}
