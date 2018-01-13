package ua.kapitonenko.app.service;

import ua.kapitonenko.app.domain.Report;

import java.util.List;

public interface ReportService extends Service {
	
	void setServiceFactory(ServiceFactory serviceFactory);
	
	boolean createZReport(Report report);
	
	long getCount();
	
	List<Report> getReportList(int offset, int limit);
}
