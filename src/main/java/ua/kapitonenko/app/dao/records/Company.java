package ua.kapitonenko.app.dao.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Company extends BaseEntity {
	
	private String pnNumber;
	private String bundleName;
	private String bundleKeyName;
	private String bundleKeyAddress;
	
	public Company() {
	}
	
	public Company(Long id, String pnNumber, String bundleName, String bundleKeyName, String bundleKeyAddress) {
		super(id);
		this.pnNumber = pnNumber;
		this.bundleName = bundleName;
		this.bundleKeyName = bundleKeyName;
		this.bundleKeyAddress = bundleKeyAddress;
	}
	
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		Company company = (Company) o;
		
		return new EqualsBuilder()
				       .append(getId(), company.getId())
				       .append(pnNumber, company.pnNumber)
				       .append(bundleName, company.bundleName)
				       .append(bundleKeyName, company.bundleKeyName)
				       .append(bundleKeyAddress, company.bundleKeyAddress)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(pnNumber)
				       .append(bundleName)
				       .append(bundleKeyName)
				       .append(bundleKeyAddress)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Company{")
				       .append("id=").append(getId())
				       .append(", pnNumber=").append(pnNumber)
				       .append(", bundleName=").append(bundleName)
				       .append(", bundleKeyName=").append(bundleKeyName)
				       .append(", bundleKeyAddress=").append(bundleKeyAddress)
				       .append("}")
				       .toString();
	}
}