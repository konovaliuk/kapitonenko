package ua.kapitonenko.app.dao.interfaces;


import ua.kapitonenko.app.dao.records.User;

public interface UserDAO extends DAO<User> {
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}