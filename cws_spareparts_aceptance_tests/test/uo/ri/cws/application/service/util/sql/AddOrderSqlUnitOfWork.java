package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;
import uo.ri.cws.application.service.util.db.JdbcTransaction;

public class AddOrderSqlUnitOfWork {

	private OrderDto dto;

	public AddOrderSqlUnitOfWork(OrderDto dto) {
		this.dto = dto;
	}

	public void execute() {
		new JdbcTransaction().execute((con) -> {
			addAggregateRoot( con );
			addParts( con );
		});
	}

	private void addAggregateRoot(Connection con) throws SQLException {
		try (PreparedStatement st = con.prepareStatement(INSERT_INTO_TORDERS)) {
            bindAggregateRootParameters(st);
			st.executeUpdate();
		}
	}

	private static final String INSERT_INTO_TORDERS =
			"INSERT INTO TORDERS "
				+ "( ID, AMOUNT, CODE, ORDEREDDATE, RECEPTIONDATE, STATE, VERSION, PROVIDER_ID ) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private void bindAggregateRootParameters(PreparedStatement st) throws SQLException {
		int i = 1;
		st.setString(i++, dto.id);
		st.setDouble(i++, dto.amount);
		st.setString(i++, dto.code);
		st.setDate(i++, Date.valueOf(dto.orderedDate));
		st.setDate(i++, toNullableDate(dto.receptionDate));
		st.setString(i++, dto.state);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.provider.id);
	}

	private Date toNullableDate(LocalDate date) {
		return date != null ? Date.valueOf( date ) : null;
	}

	private void addParts(Connection con) throws SQLException {
		try (PreparedStatement st = con.prepareStatement(INSERT_INTO_TORDERLINES)) {
			for (OrderLineDto line : dto.lines) {
				bindOrderLineParameters(st, line);
				st.executeUpdate();
			}
		}
	}

	private static final String INSERT_INTO_TORDERLINES =
			"INSERT INTO TORDERLINES "
				+ "( PRICE, QUANTITY, SPAREPART_ID, ORDER_ID ) "
				+ "VALUES ( ?, ?, ?, ? )";

	private void bindOrderLineParameters(PreparedStatement st,
			OrderLineDto line) throws SQLException {

		int i = 1;
		st.setDouble(i++, line.price );
		st.setInt(i++, line.quantity );
		st.setString(i++, line.sparePart.id);
		st.setString(i++, dto.id);
	}

}
