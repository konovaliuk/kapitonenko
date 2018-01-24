package ua.kapitonenko.app.persistence.dao;

import ua.kapitonenko.app.exceptions.DAOException;
import ua.kapitonenko.app.persistence.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.persistence.records.BaseEntity;

import java.util.List;

/**
 * Defines generic interface for performing CRUD operations.
 */
public interface DAO<E extends BaseEntity> {
	
	/**
	 * Inserts new record in database.
	 *
	 * @param entity instance of BaseEntity
	 * @return true on success, otherwise false
	 * @throws DAOException
	 */
	boolean insert(E entity);
	
	/**
	 * Updates record associated with entity in database.
	 *
	 * @param entity instance of BaseEntity
	 * @return true on success, otherwise false
	 * @throws DAOException
	 */
	boolean update(E entity);
	
	/**
	 * Deletes entity from database.
	 *
	 * @param entity instance of BaseEntity
	 * @param userId id of user who deletes entity
	 * @return true on success, otherwise false
	 * @throws DAOException
	 */
	boolean delete(E entity, Long userId);
	
	/**
	 * Returns entity associated with the given id.
	 *
	 * @param id of record.
	 * @return entity
	 * @throws DAOException
	 */
	E findOne(Long id);
	
	/**
	 * Returns list of all entities.
	 * @return list of entities
	 * @throws DAOException
	 */
	List<E> findAll();
	
	/**
	 * Returns number of records in database.
	 * @return number of records
	 * @throws DAOException
	 */
	long getCount();
	
	/**
	 * Performs query and returns list of entities.
	 * @param query sql string with placeholders
	 * @param pss values to be inserted instead of placeholders
	 * @return list of entities
	 * @throws DAOException
	 */
	List<E> findAllByQuery(String query, PreparedStatementSetter pss);
	
}