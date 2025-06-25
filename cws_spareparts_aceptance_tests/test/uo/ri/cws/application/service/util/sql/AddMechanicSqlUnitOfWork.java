package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddMechanicSqlUnitOfWork extends BaseUpdateSql<MechanicDto> {

	public AddMechanicSqlUnitOfWork(MechanicDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TMECHANICS"
				+ " ( ID, NIF, NAME, SURNAME, VERSION )"
				+ " VALUES ( ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, MechanicDto dto)
			throws SQLException {
		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.nif);
		st.setString(i++, dto.name);
		st.setString(i++, dto.surname);
		st.setLong(i++, dto.version);
	}

}
