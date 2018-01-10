package ua.kapitonenko.app.dao.mysql;

import ua.kapitonenko.app.dao.interfaces.UserDAO;
import ua.kapitonenko.app.dao.mysql.helpers.PreparedStatementSetter;
import ua.kapitonenko.app.dao.mysql.helpers.ResultSetExtractor;
import ua.kapitonenko.app.dao.tables.BaseTable;
import ua.kapitonenko.app.dao.tables.UsersTable;
import ua.kapitonenko.app.domain.records.User;

import java.sql.Connection;

public class MysqlUserDAO extends BaseDAO<User> implements UserDAO {
	private static final String FIND_ALL = "SELECT * FROM " + UsersTable.NAME;
	private static final String FIND_BY_LOGIN_AND_PASSWORD = FIND_ALL + " WHERE " +
			                                                         UsersTable.USERNAME + "=? AND " +
			                                                         UsersTable.PASSWORD + "=? AND " +
			                                                         UsersTable.ACTIVE + "=1 AND " +
			                                                         UsersTable.DELETED_AT + " IS NULL";
	
	private static final String FIND_BY_LOGIN = FIND_ALL + " WHERE "
			                                            + UsersTable.USERNAME + "=?";
	
	private static final String INSERT = "INSERT INTO " + UsersTable.NAME + " ("
			                                     + UsersTable.USER_ROLE + ", "
			                                     + UsersTable.USERNAME + ", "
			                                     + UsersTable.PASSWORD + ", "
			                                     + UsersTable.ACTIVE + ", "
			                                     + UsersTable.CREATED_AT + ")"
			                                     + " VALUES (?, ?, ?, ?, NOW())";
	
	private static final String UPDATE = "UPDATE " + UsersTable.NAME + " SET "
			                                     + UsersTable.USER_ROLE + "=?, "
			                                     + UsersTable.USERNAME + "=?, "
			                                     + UsersTable.PASSWORD + "=?, "
			                                     + UsersTable.ACTIVE + "=? "
			                                     + WHERE_ID;
	
	private static final String DELETE = "UPDATE " + UsersTable.NAME +
			                                     " SET " + UsersTable.DELETED_AT + "=NOW()" + WHERE_ID;
	
	MysqlUserDAO(Connection connection) {
		super(connection);
	}
	
	@Override
	protected String getTableName() {
		return UsersTable.NAME;
	}
	
	@Override
	protected PreparedStatementSetter getInsertStatementSetter(User entity) {
		return ps -> {
			ps.setLong(1, entity.getUserRoleId());
			ps.setString(2, entity.getUsername());
			ps.setString(3, entity.getPasswordHash());
			ps.setBoolean(4, entity.isActive());
		};
	}
	
	@Override
	protected PreparedStatementSetter getUpdateStatementSetter(User entity) {
		return ps -> {
			ps.setLong(1, entity.getUserRoleId());
			ps.setString(2, entity.getUsername());
			ps.setString(3, entity.getPasswordHash());
			ps.setBoolean(4, entity.isActive());
			ps.setLong(5, entity.getId());
		};
	}
	
	@Override
	protected String getInsertQuery() {
		return INSERT;
	}
	
	@Override
	protected String getUpdateQuery() {
		return UPDATE;
	}
	
	@Override
	public boolean delete(final User entity, Long userId) {
		int result = executeUpdate(DELETE, ps -> ps.setLong(1, entity.getId()));
		return result > 0;
	}
	
	@Override
	protected ResultSetExtractor<User> getResultSetExtractor() {
		return rs -> {
			User row = new User();
			row.setId(rs.getLong(BaseTable.ID));
			row.setUserRoleId(rs.getLong(UsersTable.USER_ROLE));
			row.setUsername(rs.getString(UsersTable.USERNAME));
			row.setPasswordHash(rs.getString(UsersTable.PASSWORD));
			row.setActive(rs.getBoolean(UsersTable.ACTIVE));
			row.setCreatedAt(rs.getTimestamp(UsersTable.CREATED_AT));
			row.setDeletedAt(rs.getTimestamp(UsersTable.DELETED_AT));
			return row;
		};
	}
	
	
	@Override
	public User findByLoginAndPassword(User user) {
		return getRow(FIND_BY_LOGIN_AND_PASSWORD, ps -> {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPasswordHash());
		}, getResultSetExtractor());
	}
	
	@Override
	public User findByUsername(User user) {
		return getRow(FIND_BY_LOGIN, ps -> {
			ps.setString(1, user.getUsername());
		}, getResultSetExtractor());
	}
	
	
}