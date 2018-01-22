package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.persistence.connection.ConnectionWrapper;
import ua.kapitonenko.app.persistence.dao.UserDAO;
import ua.kapitonenko.app.persistence.records.User;
import ua.kapitonenko.app.service.UserService;

public class UserServiceImpl extends BaseService implements UserService {
	
	UserServiceImpl() {
	}
	
	@Override
	public User createAccount(User user) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			if (Application.isAutoActivationEnabled()) {
				user.setActive(true);
			}
			
			UserDAO userDAO = getDaoFactory().getUserDAO(connection.open());
			if (userDAO.insert(user)) {
				return userDAO.findOne(user.getId());
			}
			return null;
		}
	}
	
	@Override
	public User findByLoginAndPassword(User user) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			UserDAO userDAO = getDaoFactory().getUserDAO(connection.open());
			return userDAO.findByLoginAndPassword(user);
		}
	}
	
	@Override
	public User findByUsername(User user) {
		try (ConnectionWrapper connection = getDaoFactory().getConnection()) {
			UserDAO userDAO = getDaoFactory().getUserDAO(connection.open());
			return userDAO.findByUsername(user);
		}
	}
	
}
