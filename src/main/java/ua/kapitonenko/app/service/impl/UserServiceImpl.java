package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.connection.ConnectionPool;
import ua.kapitonenko.app.dao.interfaces.UserDAO;
import ua.kapitonenko.app.domain.records.User;
import ua.kapitonenko.app.service.UserService;

import java.sql.Connection;

public class UserServiceImpl implements UserService {
	private static UserServiceImpl instance = new UserServiceImpl();
	
	private ConnectionPool pool = Application.getConnectionPool();
	
	private UserServiceImpl() {
	}
	
	public static UserServiceImpl getInstance() {
		return instance;
	}
	
	@Override
	public User createAccount(User user) {
		Connection connection = null;
		try {
			if (Application.isAutoActivationEnabled()) {
				user.setActive(true);
			}
			connection = pool.getConnection();
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
			if (userDAO.insert(user)) {
				return userDAO.findOne(user.getId());
			}
			return null;
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public User findByLoginAndPassword(User user) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
			return userDAO.findByLoginAndPassword(user);
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public User findByUsername(User user) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
			return userDAO.findByUsername(user);
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public User findById(Long id) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection);
			return userDAO.findOne(id);
		} finally {
			pool.close(connection);
		}
	}
}
