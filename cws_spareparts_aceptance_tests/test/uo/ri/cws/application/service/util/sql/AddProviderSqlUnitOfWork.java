package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddProviderSqlUnitOfWork extends BaseUpdateSql<ProviderDto> {

	public AddProviderSqlUnitOfWork(ProviderDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TPROVIDERS "
				+ "( ID, EMAIL, NAME, NIF, PHONE, VERSION ) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?)";	
	}

	@Override
	protected void bindParameters(PreparedStatement st, ProviderDto dto)
			throws SQLException {
		
		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.email);
		st.setString(i++, dto.name);
		st.setString(i++, dto.nif);
		st.setString(i++, dto.phone);
		st.setLong(i++, dto.version);
	}

}
