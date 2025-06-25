package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;

public class AddVehicleSqlUnitOfWork extends BaseUpdateSql<VehicleDto> {

	public AddVehicleSqlUnitOfWork(VehicleDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TVEHICLES"
				+ "( ID, MAKE, MODEL, PLATENUMBER, VERSION, CLIENT_ID, VEHICLETYPE_ID )"
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, VehicleDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.make);
		st.setString(i++, dto.model);
		st.setString(i++, dto.plate);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.clientId );
		st.setString(i++, dto.vehicleTypeId );
	}

}
