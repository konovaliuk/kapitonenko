package ua.kapitonenko.app.persistence.records;

public class Unit extends BaseLocalizedEntity {
	
	public Unit() {
	}
	
	public Unit(Long id, String name, String bundleName, String bundleKey) {
		super(id, name, bundleName, bundleKey);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Unit")
				       .append(super.toString())
				       .toString();
	}
}