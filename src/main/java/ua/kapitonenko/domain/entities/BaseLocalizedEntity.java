package ua.kapitonenko.domain.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class BaseLocalizedEntity extends BaseEntity {
	
	private String name;
	private String bundleName;
	private String bundleKey;
	
	public BaseLocalizedEntity() {
	}
	
	public BaseLocalizedEntity(Long id, String name, String bundleName, String bundleKey) {
		super(id);
		this.name = name;
		this.bundleName = bundleName;
		this.bundleKey = bundleKey;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBundleName() {
		return this.bundleName;
	}
	
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	public String getBundleKey() {
		return this.bundleKey;
	}
	
	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass() != o.getClass()) return false;
		
		BaseLocalizedEntity that = (BaseLocalizedEntity) o;
		
		return new EqualsBuilder()
				       .append(getId(), that.getId())
				       .append(name, that.name)
				       .append(bundleName, that.bundleName)
				       .append(bundleKey, that.bundleKey)
				       .isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(getId())
				       .append(name)
				       .append(bundleName)
				       .append(bundleKey)
				       .toHashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("{")
				       .append("id=").append(getId())
				       .append(", name=").append(name)
				       .append(", bundleName=").append(bundleName)
				       .append(", bundleKey=").append(bundleKey)
				       .toString();
	}
}