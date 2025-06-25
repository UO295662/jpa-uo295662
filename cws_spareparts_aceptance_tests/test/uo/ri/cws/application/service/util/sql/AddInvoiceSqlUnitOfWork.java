package uo.ri.cws.application.service.util.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.util.sql.base.BaseUpdateSql;

public class AddInvoiceSqlUnitOfWork extends BaseUpdateSql<InvoiceDto>{

	public AddInvoiceSqlUnitOfWork(InvoiceDto dto) {
		super( dto );
	}

	@Override
	protected String getSqlStatement() {
		return "INSERT INTO TINVOICES"
				+ " ( ID, VERSION, NUMBER, AMOUNT, VAT, DATE, STATUS )"
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void bindParameters(PreparedStatement st, InvoiceDto dto)
			throws SQLException {

		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		st.setLong(i++, dto.number);
		st.setDouble(i++, dto.amount);
		st.setDouble(i++, dto.vat);
		st.setDate(i++, Date.valueOf(dto.date) );
		st.setString(i++, dto.state.toString());
	}
	
}
