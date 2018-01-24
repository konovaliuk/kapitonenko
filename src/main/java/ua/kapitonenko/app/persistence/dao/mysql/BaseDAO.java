package ua.kapitonenko.app.persistence.dao.mysql;

import org.slf4j.LoggerFactory;
import ua.kapitonenko.app.exceptions.DAOException;
import ua.kapitonenko.app.persistence.dao.DAO;
import ua.kapitonenko.app.persistence.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.persistence.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.persistence.records.BaseEntity;
import ua.kapitonenko.app.persistence.tables.BaseTable;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of {@code DAO} interface.
 * Uses connection provided by service layer.
 * Uses TemplateMethod Pattern to get concrete implementation.
 */
public abstract class BaseDAO<E extends BaseEntity> implements DAO<E> {
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	protected static final String WHERE_ID = " WHERE " + BaseTable.ID + "=? ";
	protected static final String AND_NOT_DELETED = " AND " + BaseTable.DELETED_AT + " IS NULL";
	protected static final String WHERE_NOT_DELETED = " WHERE " + BaseTable.DELETED_AT + " IS NULL";
	
	private Connection connection;
	
	/**
	 * Constructs instance with the given connection.
	 */
	public BaseDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * TemplateMethod to get the actual name of the table.
	 */
	protected abstract String getTableName();
	
	/**
	 * TemplateMethod to get logic of columns to properties mapping from concrete implementation.
	 */
	protected abstract ResultSetExtractor<E> getResultSetExtractor();
	
	/**
	 * TemplateMethod to get insert sql string from concrete implementation.
	 */
	protected abstract String getInsertQuery();
	
	/**
	 * TemplateMethod to get placeholder values for insert string from concrete implementation.
	 */
	protected abstract PreparedStatementSetter getInsertStatementSetter(E entity);
	
	/**
	 * TemplateMethod to get placeholder values for update string from concrete implementation.
	 */
	protected abstract PreparedStatementSetter getUpdateStatementSetter(E entity);
	
	/**
	 * TemplateMethod to get update sql string from concrete implementation.
	 */
	protected abstract String getUpdateQuery();
	
	/**
	 * Constructs select all sql string using table name provided by template method.
	 */
	protected String getSelectAllQuery() {
		return "SELECT * FROM " + getTableName();
	}
	
	/**
	 * Constructs select count sql string using table name provided by template method.
	 */
	protected String getCountQuery() {
		return "SELECT COUNT(*) count FROM " + getTableName();
	}
	
	/**
	 * Constructs select with id sql string.
	 */
	protected String getSelectOneQuery() {
		return getSelectAllQuery() + WHERE_ID;
	}
	
	/**
	 * Constructs select with id sql string for not deleted records.
	 */
	protected String getSelectOneNotDeletedQuery() {
		return getSelectOneQuery() + AND_NOT_DELETED;
	}
	
	/**
	 * Constructs select all not deleted string.
	 */
	protected String getSelectAllNotDeletedQuery() {
		return getSelectAllQuery() + WHERE_NOT_DELETED;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public E findOne(final Long id) {
		return getRow(getSelectOneQuery(), ps -> ps.setLong(1, id), getResultSetExtractor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> findAll() {
		return getList(getSelectAllQuery(), getResultSetExtractor());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean insert(E entity) {
		long id = executeInsert(getInsertQuery(), getInsertStatementSetter(entity));
		if (id > 0) {
			entity.setId(id);
		}
		return id > 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(E entity) {
		int result = executeUpdate(getUpdateQuery(), getUpdateStatementSetter(entity));
		return result > 0;
	}
	
	/**
	 * Performs search all with parameters. Returns list of entities.
	 *
	 * @param sql string
	 * @param pss values of placeholders
	 * @param rse column to property mapping
	 * @return list of entities
	 * @throws DAOException
	 */
	protected List<E> getList(String sql, PreparedStatementSetter pss, ResultSetExtractor<E> rse) {
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			
			if (pss != null) {
				pss.prepare(ps);
			}
			ResultSet rs = ps.executeQuery();
			logger.debug(statementToString(ps));
			
			List<E> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rse.extract(rs));
			}
			return list;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	/**
	 * Performs search all without parameters.
	 *
	 * @param sql string
	 * @param rse column to property mapping
	 * @return list of entities
	 */
	protected List<E> getList(String sql, ResultSetExtractor<E> rse) {
		return getList(sql, null, rse);
	}
	
	/**
	 * Performs search with parameters. Returns one entity.
	 *
	 * @param sql string
	 * @param pss values of placeholders
	 * @param rse column to property mapping
	 * @return list of entities
	 * @throws DAOException
	 */
	protected E getRow(String sql, PreparedStatementSetter pss, ResultSetExtractor<E> rse) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			ResultSet rs = ps.executeQuery();
			logger.debug(statementToString(ps));
			
			if (rs.next()) {
				return rse.extract(rs);
			}
			return null;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	/**
	 * Returns id of record that corresponds to search params.
	 *
	 * @param sql string
	 * @param pss values of placeholders
	 * @return list of entities
	 * @throws DAOException
	 */
	protected Long getLong(String sql, PreparedStatementSetter pss) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			ResultSet rs = ps.executeQuery();
			logger.debug(statementToString(ps));
			
			if (rs.next()) {
				return rs.getLong(1);
			}
			return null;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	protected int executeUpdate(String sql, PreparedStatementSetter pss) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			logger.debug(statementToString(ps));
			return ps.executeUpdate();
			
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	protected long executeInsert(String sql, PreparedStatementSetter pss) {
		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			
			logger.debug(statementToString(ps));
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				return rs.getLong(1);
			}
			return -1;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCount() {
		long result = 0;
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(getCountQuery());
			logger.debug(getCountQuery());
			while (rs.next()) {
				result = rs.getLong("count");
			}
			return result;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> findAllByQuery(String query, PreparedStatementSetter pss) {
		String sql = getSelectAllQuery() + " " + query;
		return getList(sql, pss, getResultSetExtractor());
	}
	
	/**
	 * Extracts sql string from {@code PreparedStatement} object.
	 *
	 * @param ps PreparedStatement object
	 * @return string sql
	 */
	public static String statementToString(PreparedStatement ps) {
		String[] pss = ps.toString().split(": ");
		return pss[1];
	}
}
