package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.WorkOrder.WorkOrderState;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

    private List<String> workOrderIds;
    private WorkOrderRepository wrkrsRepo = Factories.repository.forWorkOrder();
    private InvoiceRepository invsRepo = Factories.repository.forInvoice();

    /**
     * Creates an invoice for the work orders indicated by its id. All the work
     * orders must exist and be in FINISHED state.
     * 
     * @param workOrderIds, the ids of the work orders to be included
     * @return the dto of the newly generated invoice
     * @throws BusinessException        if - Any of the indicated work orders
     *                                  are not in FINISHED state - There not
     *                                  exist any of the ids indicated
     * @throws IllegalArgumentException if - the list of workOrderIds is null -
     *                                  the list of workOrderIds is empty - any
     *                                  of the workOrderIds is null
     */
    public CreateInvoiceFor(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds);
        ArgumentChecks.isFalse(workOrderIds.isEmpty());
        ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));

        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {
        checks();

        List<WorkOrder> workOrders = wrkrsRepo.findByIds(workOrderIds);
        long numero = invsRepo.getNextInvoiceNumber();
        Invoice invoice = new Invoice(numero, workOrders);
        invsRepo.add(invoice);

        return DtoAssembler.toDto(invoice);
    }

    /*
     * @throws BusinessException if - Any of the indicated work orders are not
     * in FINISHED state - There not exist any of the ids indicated
     */
    private void checks() throws BusinessException {
        checkWorkOrdersExists();
        checkWorkOrderIsFinished();
    }

    private void checkWorkOrderIsFinished() throws BusinessException {
        boolean condition = true;
        for (String id : workOrderIds) {
            WorkOrder workOrder = wrkrsRepo.findById(id).get();
            if (!workOrder.getState().equals(WorkOrderState.FINISHED)) {
                condition = false;
            }
        }

        BusinessChecks.isTrue(condition,
            "Any of the workOrders are not in FINISHED state");
    }

    private void checkWorkOrdersExists() throws BusinessException {
        boolean condition = true;
        for (String id : workOrderIds) {
            if (wrkrsRepo.findById(id).isEmpty())
                condition = false;
        }
        BusinessChecks.isTrue(condition,
            "There not exist any of the ids indicated");
    }

}
