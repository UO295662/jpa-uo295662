package uo.ri.cws.application.service.util.sql.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.util.db.JdbcTransaction;

/**
 * Abstract base class for executing SQL queries that fetch multiple records.
 * 
 * Designed for inheritance. Subclasses must implement SQL query definition,
 * parameter binding, and row mapping to transform result sets into objects.
 * 
 * Use by extending this class, providing implementations for abstract methods,
 * then invoke execute() to run the query and fetch results, accessible via get().
 *
 * @param <T> The type of objects in the result list.
 * 
 * @author alb@uniovi.es
 */
public abstract class BaseFindManySql<T> implements PositionalBinding {
	private List<T> result;

	public BaseFindManySql<T> execute() {
		new JdbcTransaction().execute((con) -> {
			result = executeStatement(con);
		});
		return this;
	}

	private List<T> executeStatement(Connection con) throws SQLException {
		try (PreparedStatement st = prepareStatement( con )) {
			bindParameters(st);
			return parseResult( st.executeQuery() );
		}
	}

	private List<T> parseResult(ResultSet rs) throws SQLException {
		try {
			List<T> result = new ArrayList<>();
			
			while (rs.next()) {
				result.add( mapRow(rs) );
			}
			
			return result;
		} finally {
			rs.close();
		}
	}

	public List<T> get() {
		return result;
	}

	private PreparedStatement prepareStatement(Connection con) throws SQLException {
		return con.prepareStatement( getSqlQuery() );
	}

	protected abstract String getSqlQuery();
	protected abstract void bindParameters(PreparedStatement st) throws SQLException;
	protected abstract T mapRow(ResultSet rs) throws SQLException;

}
