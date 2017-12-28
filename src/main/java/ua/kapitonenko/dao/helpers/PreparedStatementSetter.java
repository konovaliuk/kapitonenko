package ua.kapitonenko.dao.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {
	void prepare(PreparedStatement ps) throws SQLException;
}
