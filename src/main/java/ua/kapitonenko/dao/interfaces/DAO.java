package ua.kapitonenko.dao.interfaces;

import ua.kapitonenko.domain.BaseEntity;

import java.util.List;

public interface DAO<E extends BaseEntity> {
	
	boolean insert(E entity);
	
	boolean update(E entity);
	
	boolean delete(E entity, Long userId);
	
	E findOne(Long id);
	
	List<E> findAll();
	
}