package uo.ri.cws.application.service.spare.order.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.order.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class OrderFindByProviderNif implements Command<List<OrderDto>> {

    private String nif;

    private OrderRepository orRep = Factories.repository.forOrder();

    /**
     * Returns all the orders for the provider identified by its nif
     * 
     * @param nif
     * @return a list with orders or empty if there is no order from the
     *         provider
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    public OrderFindByProviderNif(String nif) {
        ArgumentChecks.isNotNull(nif, "Invalid nif");
        this.nif = nif;
    }

    @Override
    public List<OrderDto> execute() throws BusinessException {
        return DtoAssembler.toOrdersDtoList(orRep.findByProviderNif(nif));
    }

}
