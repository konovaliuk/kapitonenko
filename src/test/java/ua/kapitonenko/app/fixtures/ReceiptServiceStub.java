package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.domain.Receipt;
import ua.kapitonenko.app.service.ReceiptService;

import java.util.List;

public class ReceiptServiceStub implements ReceiptService {
	
	@Override
	public boolean update(Receipt receipt) {
		return false;
	}
	
	@Override
	public boolean create(Receipt receipt) {
		return false;
	}
	
	@Override
	public List<Receipt> getReceiptList(int offset, int limit) {
		return null;
	}
	
	@Override
	public List<Receipt> getReceiptList(Long cashboxId) {
		return null;
	}
	
	@Override
	public List<Receipt> getReceiptList(Long reportId, Long cashboxId) {
		return null;
	}
	
	@Override
	public Receipt findOne(Long receiptId) {
		return null;
	}
	
	@Override
	public boolean cancel(Long receiptId) {
		return false;
	}
	
	@Override
	public long getCount() {
		return 0;
	}
	
	@Override
	public boolean createReturn(Receipt receipt) {
		return false;
	}
	
	@Override
	public void setDaoFactory(DAOFactory daoFactory) {
	
	}
}
