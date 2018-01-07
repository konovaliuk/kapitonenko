package ua.kapitonenko.app.dao.interfaces;


import ua.kapitonenko.app.domain.records.User;

public interface UserDAO extends DAO<User> {
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}