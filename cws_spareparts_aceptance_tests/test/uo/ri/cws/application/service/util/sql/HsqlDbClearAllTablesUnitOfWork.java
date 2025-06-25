package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class HsqlDbClearAllTablesUnitOfWork extends BaseUpdateSql<Void> {
	
	public HsqlDbClearAllTablesUnitOfWork() {
		super( null );
	}

	@Override
	protected String getSqlStatement() {
		return "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";
	}

	@Override
	protected void bindParameters(PreparedStatement st, Void dto)
			throws SQLException {
		// nothing to bind
	}

}
