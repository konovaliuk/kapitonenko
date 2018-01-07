package ua.kapitonenko.app.dao.interfaces;

import ua.kapitonenko.app.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.domain.records.BaseEntity;

import java.util.List;

public interface DAO<E extends BaseEntity> {
	
	boolean insert(E entity);
	
	boolean update(E entity);
	
	boolean delete(E entity, Long userId);
	
	E findOne(Long id);
	
	List<E> findAll();
	
	int getCount();
	
	List<E> findAllByQuery(String query, PreparedStatementSetter pss);
	
}