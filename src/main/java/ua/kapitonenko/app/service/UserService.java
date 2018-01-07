package ua.kapitonenko.app.service;

import ua.kapitonenko.app.domain.records.User;

public interface UserService {
	User createAccount(User user);
	
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
	
	User findById(Long id);
}
