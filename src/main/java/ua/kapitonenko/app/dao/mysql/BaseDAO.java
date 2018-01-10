package ua.kapitonenko.app.dao.mysql;

import org.apache.log4j.Logger;
import ua.kapitonenko.app.dao.interfaces.DAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.domain.records.BaseEntity;
import ua.kapitonenko.app.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<E extends BaseEntity> implements DAO<E> {
	private static final Logger LOGGER = Logger.getLogger(BaseDAO.class);
	
	
	protected static final String WHERE_ID = " WHERE " + BaseTable.ID + "=? ";
	protected static final String AND_NOT_DELETED = " AND " + BaseTable.DELETED_AT + " IS NULL";
	protected static final String WHERE_NOT_DELETED = " WHERE " + BaseTable.DELETED_AT + " IS NULL";
	
	private Connection connection;
	
	public BaseDAO(Connection connection) {
		this.connection = connection;
	}
	
	protected abstract String getTableName();
	
	protected abstract ResultSetExtractor<E> getResultSetExtractor();
	
	
	protected abstract String getInsertQuery();
	
	protected abstract PreparedStatementSetter getInsertStatementSetter(E entity);
	
	protected abstract PreparedStatementSetter getUpdateStatementSetter(E entity);
	
	protected abstract String getUpdateQuery();
	
	protected String getSelectAllQuery() {
		return "SELECT * FROM " + getTableName();
	}
	
	protected String getCountQuery() {
		return "SELECT COUNT(*) count FROM " + getTableName();
	}
	
	protected String getSelectOneQuery() {
		return getSelectAllQuery() + WHERE_ID;
	}
	
	protected String getSelectOneNotDeletedQuery() {
		return getSelectOneQuery() + AND_NOT_DELETED;
	}
	
	protected String getSelectAllNotDeletedQuery() {
		return getSelectAllQuery() + WHERE_NOT_DELETED;
	}
	
	public E findOne(final Long id) {
		return getRow(getSelectOneQuery(), ps -> ps.setLong(1, id), getResultSetExtractor());
	}
	
	public List<E> findAll() {
		return getList(getSelectAllQuery(), getResultSetExtractor());
	}
	
	public boolean insert(E entity) {
		long id = executeInsert(getInsertQuery(), getInsertStatementSetter(entity));
		if (id > 0) {
			entity.setId(id);
		}
		return id > 0;
	}
	
	public boolean update(E entity) {
		int result = executeUpdate(getUpdateQuery(), getUpdateStatementSetter(entity));
		return result > 0;
	}
	
	protected List<E> getList(String sql, PreparedStatementSetter pss, ResultSetExtractor<E> rse) {
		
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			
			if (pss != null) {
				pss.prepare(ps);
			}
			
			LOGGER.debug(ps.toString());
			ResultSet rs = ps.executeQuery();
			List<E> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rse.extract(rs));
			}
			return list;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	protected List<E> getList(String sql, ResultSetExtractor<E> rse) {
		return getList(sql, null, rse);
	}
	
	protected E getRow(String sql, PreparedStatementSetter pss, ResultSetExtractor<E> rse) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rse.extract(rs);
			}
			return null;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	protected Long getLong(String sql, PreparedStatementSetter pss) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			if (pss != null) {
				pss.prepare(ps);
			}
			ResultSet rs = ps.executeQuery();
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
			
			ps.executeUpdate();
			LOGGER.debug(ps.toString());
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				return rs.getLong(1);
			}
			return -1;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	public long getCount() {
		long result = 0;
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(getCountQuery());
			while (rs.next()) {
				result = rs.getLong("count");
			}
			return result;
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public List<E> findAllByQuery(String query, PreparedStatementSetter pss) {
		String sql = getSelectAllQuery() + " " + query;
		return getList(sql, pss, getResultSetExtractor());
	}
	
	
}
