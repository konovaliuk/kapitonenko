package ua.kapitonenko.app.persistence.dao;

import ua.kapitonenko.app.persistence.records.ReceiptRecord;

import java.util.List;

public interface ReceiptDAO extends DAO<ReceiptRecord> {
	List<ReceiptRecord> findAllByZReportId(Long zReportId, Long cashboxId);
	
	List<ReceiptRecord> findAllByCashboxId(Long cashboxId);
	
	List<ReceiptRecord> findAll(int offset, int limit);
}