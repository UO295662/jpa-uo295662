package uo.ri.cws.application.service.invoice.create;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.command.CreateInvoiceFor;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class InvoicingServiceImpl implements InvoicingService {

	private CommandExecutor executor = Factories.executor.forExecutor();

	@Override
	public InvoiceDto createInvoiceFor(List<String> woIds)
			throws BusinessException {

		return executor.execute( new CreateInvoiceFor( woIds) );
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif)
			throws BusinessException {
		throw new NotYetImplementedException();
	}

	@Override
	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(
			String nif) throws BusinessException {
		throw new NotYetImplementedException();
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate)
			throws BusinessException {
		throw new NotYetImplementedException();
	}

	@Override
	public Optional<InvoiceDto> findInvoiceByNumber(Long number)
			throws BusinessException {
		throw new NotYetImplementedException();
	}

	@Override
	public List<PaymentMeanDto> findPayMeansByClientNif(String nif)
			throws BusinessException {
		throw new NotYetImplementedException();
	}

	@Override
	public void settleInvoice(String invoiceId, Map<String, Double> charges)
			throws BusinessException {
		throw new NotYetImplementedException();
	}

}
