package ua.kapitonenko.dao.interfaces;


import ua.kapitonenko.domain.entities.ReceiptProduct;

import java.util.List;

public interface ReceiptProductDAO extends DAO<ReceiptProduct> {
	List<ReceiptProduct> findAllByReceiptId(Long productId);
}