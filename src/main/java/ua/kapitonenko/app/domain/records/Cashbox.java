package ua.kapitonenko.app.domain.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Cashbox extends BaseEntity {
	
	private String fnNumber;
	private String znNumber;
	private String make;
	private Date deletedAt;
	private Long deletedBy;
	
	public Cashbox() {
	}
	
	public Cashbox(String fnNumber, String znNumber, String make) {
		this.fnNumber = fnNumber;
		this.znNumber = znNumber;
		this.make = make;
	}
	
	public String getFnNumber() {
		return this.fnNumber;
	}
	
	public void setFnNumber(String fnNumber) {
		this.fnNumber = fnNumber;
	}
	
	public String getZnNumber() {
		return this.znNumber;
	}
	
	public void setZnNumber(String znNumber) {
		this.znNumber = znNumber;
	}
	
	public String getMake() {
		return this.make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
	
	public Date getDeletedAt() {
		return this.deletedAt;
	}
	
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public Long getDeletedBy() {
		return this.deletedBy;
	}
	
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		Cashbox machine = (Cashbox) o;
		
		return new EqualsBuilder()
				       .append(fnNumber, machine.fnNumber)
				       .append(znNumber, machine.znNumber)
				       .append(make, machine.make)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(fnNumber)
				       .append(znNumber)
				       .append(make)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Cashbox{")
				       .append("id=").append(getId())
				       .append(", fnNumber=").append(fnNumber)
				       .append(", znNumber=").append(znNumber)
				       .append(", make=").append(make)
				       .append(", deletedAt=").append(deletedAt)
				       .append(", deletedBy=").append(deletedBy)
				       .append("}")
				       .toString();
	}
}