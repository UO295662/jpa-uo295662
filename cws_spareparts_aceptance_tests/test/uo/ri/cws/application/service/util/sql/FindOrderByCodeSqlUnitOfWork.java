package uo.ri.cws.application.service.util.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindAggregate;

public class FindOrderByCodeSqlUnitOfWork 
			extends BaseFindAggregate<OrderDto, OrderLineDto>{

	private String code;
	public FindOrderByCodeSqlUnitOfWork(String code) {
		this.code = code;
	}
	
	@Override
	protected String getWholeSqlQuery() {
		return "SELECT * FROM TORDERS o "
					+ "INNER JOIN TPROVIDERS p ON o.PROVIDER_ID = p.ID "
					+ "WHERE o.CODE = ?";
	}

	@Override
	protected void bindWholeParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, code);
	}

	@Override
	protected OrderDto mapWholeRow(ResultSet rs) throws SQLException {
		OrderDto result = new OrderDto();
		
		result.id = rs.getString("id");
		result.version = rs.getLong("version");

		result.code = rs.getString("code");
		result.state = rs.getString("state");
		result.orderedDate = rs.getDate("ordereddate").toLocalDate();
		result.receptionDate = toNullableDate(rs.getDate("receptiondate"));
		result.amount = rs.getDouble("amount");

		result.provider.id = rs.getString("provider_id");
		result.provider.nif = rs.getString("nif");
		result.provider.name = rs.getString("name");
		
		return result;
	}

	private LocalDate toNullableDate(Date date) {
		return date != null ? date.toLocalDate() : null;
	}

	@Override
	protected String getPartsSqlQuery() {
		return "SELECT ol.quantity, ol.price, sp.id as sparepart_id, sp.code, sp.description "
			+ "FROM TORDERLINES ol "
				+ "JOIN TSPAREPARTS sp ON ol.SPAREPART_ID = sp.ID "
				+ "JOIN TORDERS o ON ol.ORDER_ID = o.ID "
			+ "WHERE o.CODE = ?";
	}

	@Override
	protected void bindPartsParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, code);
	}

	@Override
	protected OrderLineDto mapPartRow(ResultSet rs) throws SQLException {
		OrderLineDto line = new OrderLineDto();
		
		line.quantity = rs.getInt("quantity");
		line.price = rs.getDouble("price");
		line.sparePart.id = rs.getString("sparepart_id");
		line.sparePart.code = rs.getString("code");
		line.sparePart.description = rs.getString("description");
		
		return line;
	}

	@Override
	protected OrderDto assemble(OrderDto whole, List<OrderLineDto> parts) {
		whole.lines.addAll( parts );
		return whole;
	}
	
}
