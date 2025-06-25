package uo.ri.cws.application.service.util.sql.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.util.db.JdbcTransaction;

/**
 * Provides a framework for loading an aggregate (the whole and the parts) 
 * from two queries. 
 * 
 * Designed for extension, this class requires subclasses to define 
 * 		- SQL queries for the whole and parts of the aggregate, 
 *  	- result mapping for the whole and the parts, and 
 *  	- object assembly.
 * 
 * @param <T> Type of the aggregate result.
 * @param <U> Type of the components of the aggregate.
 * 
 * @author alb@uniovi.es
 */
public abstract class BaseFindAggregate<T, U> 
		implements PositionalBinding {

	private T aggregate;

	public T get() {
		return aggregate;
	}

	public BaseFindAggregate<T, U> execute() {

		new JdbcTransaction().execute((con) -> {
			T whole = executeWholeStament(con);
			List<U> parts = executePartsStatement(con);
			aggregate = assembleAggregate(whole, parts);
		});
		
		return this;
	}

	protected abstract String getWholeSqlQuery();
	protected abstract void bindWholeParameters(PreparedStatement st) throws SQLException;
	protected abstract T mapWholeRow(ResultSet rs) throws SQLException;

	protected abstract String getPartsSqlQuery();
	protected abstract void bindPartsParameters(PreparedStatement st) throws SQLException;
	protected abstract U mapPartRow(ResultSet rs) throws SQLException;

	protected abstract T assemble(T whole, List<U> parts);

	private T executeWholeStament(Connection con) throws SQLException {
		T whole;
		try (PreparedStatement st = prepareWholeStatement( con ) ) {
			bindWholeParameters(st);
			whole = parseWhole( st.executeQuery() );
		}
		return whole;
	}

	private List<U> executePartsStatement(Connection con) throws SQLException {
		List<U> parts = new ArrayList<>();
		try (PreparedStatement st = preparePartsStatement(con)) {
			bindPartsParameters(st);
			parts = parseParts( st.executeQuery() );
		}
		return parts;
	}

	private PreparedStatement preparePartsStatement(Connection con) 
			throws SQLException {
		return con.prepareStatement( getPartsSqlQuery() );
	}

	private PreparedStatement prepareWholeStatement(Connection con) 
			throws SQLException {
		return con.prepareStatement( getWholeSqlQuery() );
	}

	private T parseWhole(ResultSet rs) throws SQLException {
		try {
			return rs.next() ? mapWholeRow(rs) : null;
		} finally  {
			rs.close();
		}
	}

	private List<U> parseParts(ResultSet rs) throws SQLException {
		List<U> res = new ArrayList<>();
		try {
			while (rs.next()) {
				res.add(mapPartRow(rs));
			}
		} finally {
			rs.close();
		}
		return res;
	}

	private T assembleAggregate(T whole, List<U> parts) {
		return (whole != null) 
				? assemble(whole, parts) 
				: null;
	}

}
