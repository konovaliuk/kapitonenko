package ua.kapitonenko.app.dao.interfaces;

import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.records.BaseEntity;

import java.util.List;

public interface DAO<E extends BaseEntity> {
	
	boolean insert(E entity);
	
	boolean update(E entity);
	
	boolean delete(E entity, Long userId);
	
	E findOne(Long id);
	
	List<E> findAll();
	
	long getCount();
	
	List<E> findAllByQuery(String query, PreparedStatementSetter pss);
	
}