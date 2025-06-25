package uo.ri.cws.application.service.util.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.util.sql.base.BaseFindOneSql;

public class FindInvoiceByIdSqlUnitOfWork extends BaseFindOneSql<InvoiceDto> {

	private String id;

	public FindInvoiceByIdSqlUnitOfWork(String id) {
		this.id = id;
	}
	
	@Override
	protected String getSqlQuery() {
		return "SELECT * FROM TINVOICES WHERE ID = ?";
	}

	@Override
	protected void bindParameters(PreparedStatement st) throws SQLException {
		st.setString(FIRST_PARAMETER, id);
	}

	@Override
	protected InvoiceDto mapRow(ResultSet rs) throws SQLException {
		InvoiceDto res = new InvoiceDto();
		res.id = rs.getString("id");
		res.number = rs.getLong("number");
		res.state = rs.getString("state");
		res.amount = rs.getDouble("amount");
		res.vat = rs.getDouble("vat");
		res.date = rs.getDate("date").toLocalDate();
		res.version = rs.getLong("version");
        return res;
	}
	
}
