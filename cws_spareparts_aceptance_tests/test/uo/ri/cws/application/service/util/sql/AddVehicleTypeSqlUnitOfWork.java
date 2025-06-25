package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;

public class AddVehicleTypeSqlUnitOfWork extends BaseUpdateSql<VehicleTypeDto> {

	public AddVehicleTypeSqlUnitOfWork(VehicleTypeDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TVEHICLETYPES"
				+ " ( ID, NAME, PRICEPERHOUR, VERSION )"
				+ " VALUES ( ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, VehicleTypeDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.name);
		st.setDouble(i++, dto.pricePerHour);
		st.setLong(i++, dto.version);		
	}

}
