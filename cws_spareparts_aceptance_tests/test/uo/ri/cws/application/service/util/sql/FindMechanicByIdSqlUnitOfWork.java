package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindMechanicByIdSqlUnitOfWork extends BaseFindOneSql<MechanicDto> {

	private String id;

	public FindMechanicByIdSqlUnitOfWork(String id) {
		this.id = id;
	}

	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TMECHANICS WHERE ID = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, id);
	}

	@Override
	protected MechanicDto mapRow(ResultSet rs) throws SQLException {
		MechanicDto m = new MechanicDto();
		m.id = rs.getString("id");
		m.version = rs.getLong("version");
		m.nif = rs.getString("nif");
		m.name = rs.getString("name");
		m.surname = rs.getString("surname");
		return m;
	}

}
