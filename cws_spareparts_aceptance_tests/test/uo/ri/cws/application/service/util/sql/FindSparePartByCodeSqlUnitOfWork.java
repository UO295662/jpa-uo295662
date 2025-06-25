package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindSparePartByCodeSqlUnitOfWork extends BaseFindOneSql<SparePartDto> {

	private String code;

	public FindSparePartByCodeSqlUnitOfWork(String code) {
		this.code = code;
	}

	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TSPAREPARTS WHERE code = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, code);
	}

	@Override
	protected SparePartDto mapRow(ResultSet rs) throws SQLException {
		SparePartDto result = new SparePartDto();
		
		result.id = rs.getString("id");
		result.version = rs.getLong("version");

		result.code = rs.getString("code");
		result.description = rs.getString("description");
		result.stock = rs.getInt("stock");
		result.minStock = rs.getInt("minstock");
		result.maxStock = rs.getInt("maxstock");
		result.price = rs.getDouble("price");
		
		return result;
	}
	
}
