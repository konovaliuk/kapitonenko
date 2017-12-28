package ua.kapitonenko.domain;

public class Locale extends BaseEntity {
	private String name;
	
	public Locale() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLanguage() {
		return name.substring(0, 2);
	}
}
