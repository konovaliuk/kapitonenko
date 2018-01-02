package ua.kapitonenko.service;

import ua.kapitonenko.domain.entities.User;

public interface UserService {
	User createAccount(User user);
	
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}
