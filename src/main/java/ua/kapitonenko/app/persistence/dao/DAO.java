package ua.kapitonenko.app.persistence.dao;

import ua.kapitonenko.app.persistence.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.persistence.records.BaseEntity;

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