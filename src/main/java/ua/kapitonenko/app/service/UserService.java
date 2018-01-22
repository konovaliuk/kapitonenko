package ua.kapitonenko.app.service;

import ua.kapitonenko.app.persistence.records.User;

public interface UserService extends Service {
	
	User createAccount(User user);
	
	User findByLoginAndPassword(User user);
	
	User findByUsername(User user);
	
}
