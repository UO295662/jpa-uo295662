package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto>{

	private List<String> workOrderIds;
	private WorkOrderRepository wrkrsRepo = Factories.repository.forWorkOrder();
	private InvoiceRepository invsRepo = Factories.repository.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull( workOrderIds );
		ArgumentChecks.isFalse( workOrderIds.isEmpty() );
		ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));
		
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
