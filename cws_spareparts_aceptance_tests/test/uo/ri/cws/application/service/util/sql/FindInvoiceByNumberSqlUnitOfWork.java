package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindInvoiceByNumberSqlUnitOfWork extends BaseFindOneSql<InvoiceDto> {

	private Long number;

	public FindInvoiceByNumberSqlUnitOfWork(String id) {
		this.number = Long.parseLong(id);
	}

	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TINVOICES WHERE number = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setLong(FIRST_PARAMETER, number);
	}

	@Override
	protected InvoiceDto mapRow(ResultSet rs) throws SQLException {
		InvoiceDto result = new InvoiceDto();
		result.id = rs.getString("id");
		result.number = rs.getLong("number");
		result.state = rs.getString("status");
		result.amount = rs.getDouble("amount");
		return result;
	}

}
