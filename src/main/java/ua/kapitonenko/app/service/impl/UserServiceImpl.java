package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.connection.ConnectionWrapper;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.interfaces.UserDAO;
import ua.kapitonenko.app.domain.records.User;
import ua.kapitonenko.app.service.UserService;

public class UserServiceImpl implements UserService {
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public User createAccount(User user) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			if (Application.isAutoActivationEnabled()) {
				user.setActive(true);
			}
			
			UserDAO userDAO = daoFactory.getUserDAO(connection.open());
			if (userDAO.insert(user)) {
				return userDAO.findOne(user.getId());
			}
			return null;
		}
	}
	
	@Override
	public User findByLoginAndPassword(User user) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			UserDAO userDAO = daoFactory.getUserDAO(connection.open());
			return userDAO.findByLoginAndPassword(user);
		}
	}
	
	@Override
	public User findByUsername(User user) {
		try (ConnectionWrapper connection = daoFactory.getConnection()) {
			UserDAO userDAO = daoFactory.getUserDAO(connection.open());
			return userDAO.findByUsername(user);
		}
	}
	
}
