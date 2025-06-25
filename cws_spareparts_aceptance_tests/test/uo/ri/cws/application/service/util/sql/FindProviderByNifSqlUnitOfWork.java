package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindProviderByNifSqlUnitOfWork extends BaseFindOneSql<ProviderDto> {

	private String nif;

	public FindProviderByNifSqlUnitOfWork(String nif) {
		this.nif = nif;
	}

	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TPROVIDERS WHERE NIF = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, nif);
	}

	@Override
	protected ProviderDto mapRow(ResultSet rs) throws SQLException {
		ProviderDto result = new ProviderDto();
		result.id = rs.getString("id");
		result.version = rs.getLong("version");

		result.email = rs.getString("email");
		result.name = rs.getString("name");
		result.nif = rs.getString("nif");
		result.phone = rs.getString("phone");
		return result;
	}
	
}
