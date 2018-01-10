package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.UserDAO;
import ua.kapitonenko.app.domain.records.User;
import ua.kapitonenko.app.service.UserService;

public class UserServiceImpl implements UserService {
	
	@Override
	public User createAccount(User user) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			if (Application.isAutoActivationEnabled()) {
				user.setActive(true);
			}
			
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection.open());
			if (userDAO.insert(user)) {
				return userDAO.findOne(user.getId());
			}
			return null;
		}
	}
	
	@Override
	public User findByLoginAndPassword(User user) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection.open());
			return userDAO.findByLoginAndPassword(user);
		}
	}
	
	@Override
	public User findByUsername(User user) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection.open());
			return userDAO.findByUsername(user);
		}
	}
	
	@Override
	public User findById(Long id) {
		try (ConnectionWrapper connection = Application.getConnection()) {
			UserDAO userDAO = Application.getDAOFactory().getUserDAO(connection.open());
			return userDAO.findOne(id);
		}
	}
	
}
