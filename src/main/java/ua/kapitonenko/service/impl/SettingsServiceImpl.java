package ua.kapitonenko.service.impl;

import org.apache.log4j.Logger;
import ua.kapitonenko.Application;
import ua.kapitonenko.connection.ConnectionPool;
import ua.kapitonenko.dao.interfaces.CashboxDAO;
import ua.kapitonenko.dao.interfaces.LocaleDAO;
import ua.kapitonenko.dao.interfaces.UserRoleDAO;
import ua.kapitonenko.domain.*;
import ua.kapitonenko.service.SettingsService;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsServiceImpl implements SettingsService {
	private static final Logger LOGGER = Logger.getLogger(SettingsService.class);
	private static SettingsServiceImpl instance = new SettingsServiceImpl();
	private ConnectionPool pool = Application.getConnectionPool();
	
	private SettingsServiceImpl() {
	}
	
	public static SettingsServiceImpl getInstance() {
		return instance;
	}
	
	@Override
	public Map<Long, String> getRolesMap() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			UserRoleDAO roleDAO = Application.getDAOFactory().getUserRoleDAO(connection);
			List<UserRole> list = roleDAO.findAll();
			return list.stream().collect(
					Collectors.toMap(BaseEntity::getId, BaseLocalizedEntity::getBundleKey));
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public Cashbox findCashbox(Long id) {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			CashboxDAO cashboxDAO = Application.getDAOFactory().getCashboxDao(connection);
			return cashboxDAO.findOne(id);
		} finally {
			pool.close(connection);
		}
	}
	
	@Override
	public Map<String, String> getLocaleMap() {
		Connection connection = null;
		try {
			connection = pool.getConnection();
			LocaleDAO localeDAO = Application.getDAOFactory().getLocaleDAO(connection);
			List<Locale> list = localeDAO.findAll();
			return list.stream().collect(
					Collectors.toMap(Locale::getLanguage, Locale::getName));
		} finally {
			pool.close(connection);
		}
	}
}
