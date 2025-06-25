package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.SubstitutionUtil.SubstitutionDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddSubstitutionSqlUnitOfWork extends BaseUpdateSql<SubstitutionDto> {

	public AddSubstitutionSqlUnitOfWork(SubstitutionDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TSUBSTITUTIONS "
				+ "(ID, QUANTITY, VERSION, SPAREPART_ID, INTERVENTION_ID)"
				+ "VALUES (?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, SubstitutionDto dto)
			throws SQLException {
		
		int i = 1;
		st.setString(i++, dto.id);
		st.setInt(i++, dto.quantity);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.sparePartId);
		st.setString(i++, dto.interventionId);
	}
}
