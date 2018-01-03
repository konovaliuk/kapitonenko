package ua.kapitonenko.service;

import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.entities.Receipt;

import java.util.List;

public interface ReceiptService {
	
	boolean update(ReceiptCalculator calculator);
	
	boolean create(ReceiptCalculator calculator);
	
	List<ReceiptCalculator> getReceiptList(int offset, int limit, Long cashboxId);
	
	Receipt findById(Long receiptId);
	
	boolean cancel(Long receiptId);
	
	int getReceiptsCount();
}
