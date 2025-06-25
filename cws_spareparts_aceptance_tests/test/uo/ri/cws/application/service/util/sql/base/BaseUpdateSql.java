package uo.ri.cws.application.service.util.sql.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.db.JdbcTransaction;

/**
 * Abstract base class for performing update operations in a database.
 * Subclasses must implement SQL statement definition and parameter binding. 
 *
 * @param <T> The type of the DTO used for update parameters.
 * 
 * @author alb@uniovi.es
 */
public abstract class BaseUpdateSql<T> {

	private T dto;

	public BaseUpdateSql(T dto) {
		this.dto = dto;
	}

	public void execute() {
		new JdbcTransaction().execute((con) -> {
			
			try ( PreparedStatement st = prepare( con )) {
                bindParameters( st, dto );
                st.executeUpdate();
            }
		});
	}

	private PreparedStatement prepare(Connection con) throws SQLException {
		return con.prepareStatement( getSqlStatement() );
	}

	protected abstract String getSqlStatement();
	protected abstract void bindParameters(PreparedStatement st, T dto) throws SQLException;

}
