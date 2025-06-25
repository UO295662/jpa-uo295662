package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;

public class FindWorkOrderByIdSqlUnitOfWork extends BaseFindOneSql<WorkOrderDto> {

	private String id;

	public FindWorkOrderByIdSqlUnitOfWork(String id) {
		this.id = id;
	}

	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TWORKORDERS WHERE ID = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, id);
	}

	@Override
	protected WorkOrderDto mapRow(ResultSet rs) throws SQLException {
		WorkOrderDto result = new WorkOrderDto();
		
		result.id = rs.getString("id");
		result.version = rs.getLong("version");

		result.vehicleId = rs.getString("vehicle_id");
		result.date = rs.getTimestamp("date").toLocalDateTime();
		result.description = rs.getString("description");
		result.amount = rs.getDouble("amount");
		result.invoiceId = rs.getString("invoice_id");
		result.state = rs.getString("state");
		result.mechanicId = rs.getString("mechanic_id");

		return result;
	}
}
