package ua.kapitonenko.app.persistence.records;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable, Comparable<BaseEntity> {
	
	private Long id;
	
	public BaseEntity() {
	}
	
	public BaseEntity(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(BaseEntity o) {
		return id.compareTo(o.id);
	}
}