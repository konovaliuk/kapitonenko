package ua.kapitonenko.app.persistence.dao;


import ua.kapitonenko.app.persistence.records.ProductLocale;

import java.util.List;

public interface ProductLocaleDAO extends DAO<ProductLocale> {

	List<ProductLocale> findByProductAndKey(Long id, String key);
}