package ua.kapitonenko.app.domain.records;

public class ReceiptType extends BaseLocalizedEntity {
	public ReceiptType() {
	}
	
	public ReceiptType(Long id, String name, String bundleName, String bundleKey) {
		super(id, name, bundleName, bundleKey);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("ReceiptType")
				       .append(super.toString())
				       .toString();
	}
	
}