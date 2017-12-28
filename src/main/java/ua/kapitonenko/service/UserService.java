package ua.kapitonenko.service;

import ua.kapitonenko.domain.User;

public interface UserService {
	User createAccount(User user);
	
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
}
