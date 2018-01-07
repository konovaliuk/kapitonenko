package ua.kapitonenko.service.impl;

import ua.kapitonenko.service.ReportService;

public class ReportServiceImpl implements ReportService {
	private static ReportServiceImpl instance = new ReportServiceImpl();
	
	private ReportServiceImpl() {
	}
	
	public static ReportServiceImpl getInstance() {
		return instance;
	}
}
