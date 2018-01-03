package ua.kapitonenko.dao.interfaces;

import ua.kapitonenko.dao.helpers.PreparedStatementSetter;
import ua.kapitonenko.domain.entities.BaseEntity;

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