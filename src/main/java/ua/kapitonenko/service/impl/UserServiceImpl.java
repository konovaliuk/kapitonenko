package ua.kapitonenko.service.impl;

import ua.kapitonenko.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.dao.interfaces.UserDAO;
import ua.kapitonenko.domain.User;
import ua.kapitonenko.service.UserService;

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
			connection = pool.getConnection();
			user.setActive(true);
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
	
}
