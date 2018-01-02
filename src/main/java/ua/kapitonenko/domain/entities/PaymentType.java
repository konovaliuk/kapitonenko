package ua.kapitonenko.domain.entities;

public class PaymentType extends BaseLocalizedEntity {
	
	public PaymentType() {
	}
	
	public PaymentType(Long id, String name, String bundleName, String bundleKey) {
		super(id, name, bundleName, bundleKey);
	}
	
	@Override
	public String toString() {
		return new StringBuilder("PaymentType")
				       .append(super.toString())
				       .toString();
	}
	
}