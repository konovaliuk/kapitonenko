package ua.kapitonenko.app.fixtures;

import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.dao.records.User;
import ua.kapitonenko.app.service.UserService;

public class UserServiceStub implements UserService {
	@Override
	public User createAccount(User user) {
		return null;
	}
	
	@Override
	public User findByLoginAndPassword(User user) {
		return null;
	}
	
	@Override
	public User findByUsername(User user) {
		return null;
	}
	
	@Override
	public void setDaoFactory(DAOFactory daoFactory) {
	
	}
}
