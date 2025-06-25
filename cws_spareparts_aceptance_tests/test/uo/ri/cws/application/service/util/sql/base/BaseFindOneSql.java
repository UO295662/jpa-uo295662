package uo.ri.cws.application.service.util.sql.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.db.JdbcTransaction;

/**
 * Abstract base for executing SQL queries that return a single record.
 *  
 * Designed for inheritance. Subclasses must define the SQL query, parameter 
 * binding and map the result to an object of type T. 
 * 
 * @param <T> The type of the result object.
 * 
 * @author alb@uniovi.es
 */
public abstract class BaseFindOneSql<T> implements PositionalBinding {
	private T result;

	public BaseFindOneSql<T> execute() {
		new JdbcTransaction().execute((con) -> {
			result = executeStatement(con);
		});
		return this;
	}

	public T get() {
		return result;
	}

	private T executeStatement(Connection con) throws SQLException {
		try (PreparedStatement st = prepareStatement( con )) {
			bindParameters(st);
			return parseResult( st.executeQuery() );
		}
	}

	private T parseResult(ResultSet rs) throws SQLException {
		try {
			return rs.next() ? mapRow(rs) : null;
		} finally {
			rs.close();
		}
	}

	private PreparedStatement prepareStatement(Connection con) throws SQLException {
		return con.prepareStatement( getSqlQuery() );
	}

	protected abstract String getSqlQuery();
	protected abstract void bindParameters(PreparedStatement st) throws SQLException;
	protected abstract T mapRow(ResultSet rs) throws SQLException;

}
