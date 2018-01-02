package ua.kapitonenko.service;

import ua.kapitonenko.domain.ReceiptCalculator;

public interface ReceiptService {
	
	boolean updateReceipt(ReceiptCalculator calculator);
	
	boolean createReceipt(ReceiptCalculator calculator);
}
