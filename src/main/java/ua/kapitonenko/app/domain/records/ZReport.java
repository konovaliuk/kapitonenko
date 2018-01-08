package ua.kapitonenko.app.domain.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.Date;

public class ZReport extends BaseEntity {
	
	private Long cashboxId;
	private BigDecimal cashBalance;
	private Date createdAt;
	private Long createdBy;
	
	public ZReport() {
	}
	
	public ZReport(Long id, Long cashboxId, BigDecimal cashBalance, Long createdBy) {
		super(id);
		this.cashboxId = cashboxId;
		this.cashBalance = cashBalance;
		this.createdBy = createdBy;
	}
	
	public Long getCashboxId() {
		return cashboxId;
	}
	
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}
	
	public BigDecimal getCashBalance() {
		return cashBalance;
	}
	
	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		ZReport zReport = (ZReport) o;
		
		return new EqualsBuilder()
				       .append(getId(), zReport.getId())
				       .append(cashboxId, zReport.cashboxId)
				       .append(cashBalance, zReport.cashBalance)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(cashboxId)
				       .append(cashBalance)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ZReport{")
				       .append("id=").append(getId())
				       .append(", cashboxId=").append(cashboxId)
				       .append(", cashBalance=").append(cashBalance)
				       .append(", createdAt=").append(createdAt)
				       .append(", createdBy=").append(createdBy)
				       .append("}")
				       .toString();
	}
}