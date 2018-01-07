package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.service.ReportService;

public class ReportServiceImpl implements ReportService {
	private static ReportServiceImpl instance = new ReportServiceImpl();
	
	private ReportServiceImpl() {
	}
	
	public static ReportServiceImpl getInstance() {
		return instance;
	}
}
