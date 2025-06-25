package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.util.InterventionUtil.InterventionDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddInterventionSqlUnitOfWork extends BaseUpdateSql<InterventionDto> {

	public AddInterventionSqlUnitOfWork(InterventionDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TINTERVENTIONS"
				+ " ( ID, DATE, MINUTES, VERSION, MECHANIC_ID, WORKORDER_ID )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, InterventionDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setTimestamp(i++, Timestamp.valueOf( dto.date ));
		st.setInt(i++, dto.minutes);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.mechanicId);
		st.setString(i++, dto.workOrderId);
	}

}
