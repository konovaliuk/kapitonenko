package ua.kapitonenko.app.persistence.dao.mysql.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<E> {
	E extract(ResultSet rs) throws SQLException;
}
