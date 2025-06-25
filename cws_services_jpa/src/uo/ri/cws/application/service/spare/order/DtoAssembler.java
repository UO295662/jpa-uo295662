package uo.ri.cws.application.service.spare.order;

import java.util.List;

import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderedProviderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderedSpareDto;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.OrderLine;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;

public class DtoAssembler {

    public static OrderDto toDto(Order o) {
        OrderDto dto = new OrderDto();

        dto.id = o.getId();
        dto.version = o.getVersion();

        dto.code = o.getCode();
        dto.orderedDate = o.getOrderedDate();
        dto.receptionDate = o.getReceptionDate();
        dto.state = o.getState().toString();
        dto.amount = o.getAmount();

        for (OrderLine line : o.getOrderLines()) {
            dto.lines.add(toDto(line));
        }

        dto.provider = toOrderedProviderDto(o.getProvider());

        return dto;
    }

    public static OrderLineDto toDto(OrderLine line) {
        OrderLineDto dto = new OrderLineDto();

        dto.quantity = line.getQuantity();
        dto.price = line.getPrice();
        dto.sparePart = toOrderedSpareDto(line.getSparePart());

        return dto;
    }

    public static OrderedSpareDto toOrderedSpareDto(SparePart s) {
        OrderedSpareDto dto = new OrderedSpareDto();

        dto.id = s.getId();
        dto.code = s.getCode();
        dto.description = s.getDescription();

        return dto;
    }

    public static OrderedProviderDto toOrderedProviderDto(Provider p) {
        OrderedProviderDto dto = new OrderedProviderDto();

        dto.id = p.getId();
        dto.name = p.getName();
        dto.nif = p.getNif();

        return dto;
    }

    public static List<OrderDto> toOrdersDtoList(List<Order> list) {
        return list.stream().map(o -> toDto(o)).toList();
    }

}
