package ua.kapitonenko.app.service;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.domain.records.ReceiptRecord;
import ua.kapitonenko.app.domain.records.ReceiptType;

import java.util.List;

public interface ReceiptService {
	
	boolean update(Receipt calculator);
	
	boolean create(Receipt calculator);
	
	List<Receipt> getReceiptList(int offset, int limit);
	
	List<Receipt> getReceiptList(int offset, int limit, Long cashboxId);
	
	ReceiptRecord findById(Long receiptId);
	
	boolean cancel(Long receiptId);
	
	int getReceiptsCount();
	
	List<Receipt> getSales(Long cashboxId);
	
	List<Receipt> getRefunds(Long cashboxId);
	
	List<Receipt> getReceiptList(String sql, PreparedStatementSetter ps);
	
	ReceiptType findReceiptType(Long id);
}
