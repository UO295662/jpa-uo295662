package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindSuppyByNifAndCodeSqlUnitOfWork extends BaseFindOneSql<SupplyDto> {

	private String nif;
	private String code;

	public FindSuppyByNifAndCodeSqlUnitOfWork(String nif, String code) {
		this.nif = nif;
		this.code = code;
	}

	@Override
	protected String getSqlQuery() {
		return "select * " 
			+ "FROM TSupplies s "
				+ "inner join TProviders p on s.provider_id = p.id "
				+ "inner join TSpareparts sp on s.sparepart_id = sp.id "
			+ "where p.nif = ? "
				+ "and sp.code = ? ";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, nif);
		st.setString(SECOND_PARAMETER, code);
	}

	@Override
	protected SupplyDto mapRow(ResultSet rs) throws SQLException {
		SupplyDto result = new SupplyDto();
		
		result = new SupplyDto();
		result.id = rs.getString("id");
		result.version = rs.getLong("version");
		result.price = rs.getDouble("price");
		result.deliveryTerm = rs.getInt("deliveryTerm");
		
		result.provider = new SupplyDto.SupplierProviderDto();
		result.provider.id = rs.getString("provider_id");
		result.provider.nif = rs.getString("nif");
		result.provider.name = rs.getString("name");

		result.sparePart = new SupplyDto.SuppliedSparePartDto();
		result.sparePart.id = rs.getString("sparepart_id");
		result.sparePart.code = rs.getString("code");
		result.sparePart.description = rs.getString("description");

		return result;
	}

}
