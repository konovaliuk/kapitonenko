package ua.kapitonenko.app.persistence.dao;


import ua.kapitonenko.app.persistence.records.User;

public interface UserDAO extends DAO<User> {
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}