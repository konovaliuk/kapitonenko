package ua.kapitonenko.domain;

public class Company extends BaseEntity {
	
	private String pnNumber;
	private String bundleName;
	private String bundleKeyName;
	private String bundleKeyAddress;
	
	public String getPnNumber() {
		return this.pnNumber;
	}
	
	public void setPnNumber(String pnNumber) {
		this.pnNumber = pnNumber;
	}
	
	public String getBundleName() {
		return this.bundleName;
	}
	
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	public String getBundleKeyName() {
		return this.bundleKeyName;
	}
	
	public void setBundleKeyName(String bundleKeyName) {
		this.bundleKeyName = bundleKeyName;
	}
	
	public String getBundleKeyAddress() {
		return this.bundleKeyAddress;
	}
	
	public void setBundleKeyAddress(String bundleKeyAddress) {
		this.bundleKeyAddress = bundleKeyAddress;
	}
	
}