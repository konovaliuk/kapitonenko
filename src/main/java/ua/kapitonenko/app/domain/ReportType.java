package ua.kapitonenko.app.domain;

import ua.kapitonenko.app.config.keys.Keys;

public enum ReportType {
	X_REPORT(Keys.X_REPORT),
	Z_REPORT(Keys.Z_REPORT);
	
	String label;
	
	ReportType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
