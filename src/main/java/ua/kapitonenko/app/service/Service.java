package ua.kapitonenko.app.service;

import ua.kapitonenko.app.persistence.dao.DAOFactory;

public interface Service {
	
	void setDaoFactory(DAOFactory daoFactory);

}
