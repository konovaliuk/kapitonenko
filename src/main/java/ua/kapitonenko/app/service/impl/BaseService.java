package ua.kapitonenko.app.service.impl;

import ua.kapitonenko.app.config.Application;
import ua.kapitonenko.app.dao.interfaces.DAOFactory;
import ua.kapitonenko.app.domain.ModelFactory;
import ua.kapitonenko.app.service.ServiceFactory;

public class BaseService {
	
	private DAOFactory daoFactory = Application.getDAOFactory();
	private ServiceFactory serviceFactory;
	private ModelFactory modelFactory;
	
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	protected ServiceFactory getServiceFactory() {
		if (serviceFactory == null) {
			serviceFactory = Application.getServiceFactory();
		}
		return serviceFactory;
	}
	
	protected ModelFactory getModelFactory() {
		if (modelFactory == null) {
			modelFactory = Application.getModelFactory();
		}
		return modelFactory;
	}
	
	protected DAOFactory getDaoFactory() {
		return daoFactory;
	}
	
	
}
