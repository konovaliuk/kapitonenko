package ua.kapitonenko.domain;

public abstract class BaseEntity extends Model {
	
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
	
	
}