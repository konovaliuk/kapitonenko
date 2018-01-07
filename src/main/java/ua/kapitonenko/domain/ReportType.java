package ua.kapitonenko.domain;

import ua.kapitonenko.config.keys.Keys;

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
