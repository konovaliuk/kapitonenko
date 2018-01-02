package ua.kapitonenko.dao.interfaces;


import ua.kapitonenko.domain.entities.User;

public interface UserDAO extends DAO<User> {
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}