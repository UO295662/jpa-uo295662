package uo.ri.cws.application.service.spare.order.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.order.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Order;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class OrderFindByCode implements Command<Optional<OrderDto>> {

    private String code;

    private OrderRepository orRep = Factories.repository.forOrder();

    /**
     * @param code
     * @return the order identified by the code or Optional.empty() if does not
     *         exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    public OrderFindByCode(String code) {
        ArgumentChecks.isNotNull(code, "null code");
        this.code = code;
    }

    @Override
    public Optional<OrderDto> execute() throws BusinessException {
        Optional<Order> optionalOrder = orRep.findByCode(code);
        if (optionalOrder != null && !optionalOrder.isEmpty()) {
            return Optional
                .of(DtoAssembler.toDto(orRep.findByCode(code).get()));
        }
        return Optional.empty();
    }

}
