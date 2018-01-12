package ua.kapitonenko.app.service;

import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.domain.Receipt;

import java.util.List;

public interface ReceiptService extends Service {
	
	void setServiceFactory(ServiceFactory serviceFactory);
	
	void setDaoFactory(DAOFactory daoFactory);
	
	boolean update(Receipt receipt);
	
	boolean create(Receipt receipt);
	
	List<Receipt> getReceiptList(int offset, int limit);
	
	List<Receipt> getReceiptList(Long cashboxId);
	
	Receipt findOne(Long receiptId);
	
	boolean cancel(Long receiptId);
	
	long getCount();
}
