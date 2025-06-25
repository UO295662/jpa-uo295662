package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClientNif
    implements Command<List<InvoicingWorkOrderDto>> {

    private String nif;

    private WorkOrderRepository wrksRepo = Factories.repository.forWorkOrder();

    /**
     * Returns a list with not invoiced work orders of all the client's vehicles
     * 
     * @param nif of the client
     * @return a list of InvoicingWorkOrderDto or empty list if there is none
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if nif is null
     */
    public FindNotInvoicedWorkOrdersByClientNif(String nif) {
        ArgumentChecks.isNotNull(nif);
        this.nif = nif;
    }

    @Override
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
        return DtoAssembler.toInvoicingWorkOrderDtoList(
            wrksRepo.findNotInvoicedByClientNif(nif));
    }

}
