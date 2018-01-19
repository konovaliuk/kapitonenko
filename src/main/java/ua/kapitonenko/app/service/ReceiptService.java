package ua.kapitonenko.app.service;

import ua.kapitonenko.app.domain.Receipt;

import java.util.List;

public interface ReceiptService extends Service {
	
	boolean update(Receipt receipt);
	
	boolean create(Receipt receipt);
	
	List<Receipt> getReceiptList(int offset, int limit);
	
	List<Receipt> getReceiptList(Long cashboxId);
	
	List<Receipt> getReceiptList(Long reportId, Long cashboxId);
	
	Receipt findOne(Long receiptId);
	
	boolean cancel(Long receiptId);
	
	long getCount();
	
	boolean createReturn(Receipt receipt);
}
