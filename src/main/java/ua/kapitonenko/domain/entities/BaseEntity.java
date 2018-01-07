package ua.kapitonenko.domain.entities;

import ua.kapitonenko.domain.Model;

import java.io.Serializable;

public abstract class BaseEntity extends Model implements Serializable, Comparable<BaseEntity> {
	
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