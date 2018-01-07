package ua.kapitonenko.service;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.domain.ReceiptCalculator;
import ua.kapitonenko.domain.entities.Receipt;
import ua.kapitonenko.domain.entities.ReceiptType;

import java.util.List;

public interface ReceiptService {
	
	boolean update(ReceiptCalculator calculator);
	
	boolean create(ReceiptCalculator calculator);
	
	List<ReceiptCalculator> getReceiptList(int offset, int limit);
	
	List<ReceiptCalculator> getReceiptList(int offset, int limit, Long cashboxId);
	
	Receipt findById(Long receiptId);
	
	boolean cancel(Long receiptId);
	
	int getReceiptsCount();
	
	List<ReceiptCalculator> getSales(Long cashboxId);
	
	List<ReceiptCalculator> getRefunds(Long cashboxId);
	
	List<ReceiptCalculator> getReceiptList(String sql, PreparedStatementSetter ps);
	
	ReceiptType findReceiptType(Long id);
}
