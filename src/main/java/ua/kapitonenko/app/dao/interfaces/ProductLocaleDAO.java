package ua.kapitonenko.app.dao.interfaces;


import ua.kapitonenko.app.domain.records.ProductLocale;

import java.util.List;

public interface ProductLocaleDAO extends DAO<ProductLocale> {

	List<ProductLocale> findByProductAndKey(Long id, String key);
}