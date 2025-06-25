package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class UpdateSparePartSqlUnitOfWork extends BaseUpdateSql<SparePartDto> {

	public UpdateSparePartSqlUnitOfWork(SparePartDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "UPDATE TSPAREPARTS "
                + "SET code = ?, description = ?, "
                	+ "maxstock = ?, minstock = ?, stock = ?, "
                	+ "price = ?, version = ? "
                + "WHERE id = ? AND version = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st, SparePartDto dto)
			throws SQLException {
		
		int i = 1;
		st.setString(i++, dto.code);
		st.setString(i++, dto.description);
		st.setInt(i++, dto.maxStock);
		st.setInt(i++, dto.minStock);
		st.setInt(i++, dto.stock);
		st.setDouble(i++, dto.price);
		st.setDouble(i++, dto.version + 1);
		
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		
		dto.version++;
	}

}
