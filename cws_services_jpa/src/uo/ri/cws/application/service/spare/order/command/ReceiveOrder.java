package uo.ri.cws.application.service.spare.order.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.order.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.Order.OrderState;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class ReceiveOrder implements Command<OrderDto> {

    private String code;

    private OrderRepository orRep = Factories.repository.forOrder();

    /**
     * Updates prices and stocks of all involved spare parts and updates the
     * order reception date to today and its state is changed to RECEIVED
     * 
     * @param code
     * @return the order dto with the updated state
     * @throws BusinessException        in the following cases: - the order does
     *                                  not exist - the order is not in state
     *                                  PENDING
     * @throws IllegalArgumentException if the code is null
     */
    public ReceiveOrder(String code) {
        ArgumentChecks.isNotNull(code, "Invalid code");

        this.code = code;
    }

    @Override
    public OrderDto execute() throws BusinessException {
        checks();

        Order or = orRep.findByCode(code).get();

        or.receive();

        return DtoAssembler.toDto(or);
    }

    private void checks() throws BusinessException {
        Optional<Order> opOr = orRep.findByCode(code);
        BusinessChecks.exists(opOr, "order don´t exists");
        BusinessChecks.isTrue(opOr.get().getState().equals(OrderState.PENDING),
            "el estado de la order no es PENDING");
    }

}
