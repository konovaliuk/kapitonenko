package ua.kapitonenko.app.dao.records;


public class UserRole extends BaseLocalizedEntity {
	
	public UserRole() {
	}
	
	public UserRole(Long id, String name, String bundleName, String bundleKey) {
		super(id, name, bundleName, bundleKey);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("UserRole")
				       .append(super.toString())
				       .toString();
	}
}