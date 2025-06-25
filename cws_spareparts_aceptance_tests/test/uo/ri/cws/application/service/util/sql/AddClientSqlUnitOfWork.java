package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddClientSqlUnitOfWork extends BaseUpdateSql<ClientDto> {

	public AddClientSqlUnitOfWork(ClientDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TCLIENTS"
				+ "( ID, NIF, EMAIL, NAME, PHONE, SURNAME, VERSION, CITY, STREET, ZIPCODE )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, ClientDto dto) 
			throws SQLException {
		
		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.nif);
		st.setString(i++, dto.email);
		st.setString(i++, dto.phone);
		st.setString(i++, dto.name);
		st.setString(i++, dto.surname);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.addressCity);
		st.setString(i++, dto.addressStreet);
		st.setString(i++, dto.addressZipcode);
	}

}
