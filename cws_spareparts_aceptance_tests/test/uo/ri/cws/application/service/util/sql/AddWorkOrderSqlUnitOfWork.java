package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;

public class AddWorkOrderSqlUnitOfWork extends BaseUpdateSql<WorkOrderDto> {

	public AddWorkOrderSqlUnitOfWork(WorkOrderDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TWORKORDERS"
				+ " ( ID, AMOUNT, DATE, DESCRIPTION, STATE, VERSION, VEHICLE_ID, MECHANIC_ID, INVOICE_ID )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, WorkOrderDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setDouble(i++, dto.amount);
		st.setTimestamp(i++, Timestamp.valueOf( dto.date ));
		st.setString(i++, dto.description);
		st.setString(i++, dto.state);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.vehicleId);
		
		st.setString(i++, dto.mechanicId);
		st.setString(i++, dto.invoiceId);
	}

}
