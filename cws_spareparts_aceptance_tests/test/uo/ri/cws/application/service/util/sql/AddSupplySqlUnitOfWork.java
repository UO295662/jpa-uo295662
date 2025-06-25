package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddSupplySqlUnitOfWork extends BaseUpdateSql<SupplyDto> {

	public AddSupplySqlUnitOfWork(SupplyDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TSUPPLIES "
				+ "( ID, DELIVERYTERM, PRICE, VERSION, PROVIDER_ID, SPAREPART_ID ) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, SupplyDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setInt(i++, dto.deliveryTerm );
		st.setDouble(i++, dto.price );
		st.setLong(i++, dto.version );
		st.setString(i++, dto.provider.id );
		st.setString(i++, dto.sparePart.id );
	}

}
