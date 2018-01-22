package ua.kapitonenko.app.persistence.records;

public class LocaleRecord extends BaseEntity {
	private String name;
	private String language;
	
	public LocaleRecord() {
	}
	
	public LocaleRecord(Long id, String name) {
		super(id);
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		this.language = name.substring(0, 2);
	}
	
	public String getLanguage() {
		return language;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Locale{")
					   .append("id=").append(getId())
				       .append(", name=").append(name)
				       .append(", language=").append(language)
				       .append("}")
				       .toString();
	}
}
