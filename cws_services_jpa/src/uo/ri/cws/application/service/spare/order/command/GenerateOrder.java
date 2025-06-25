package uo.ri.cws.application.service.spare.order.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.order.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;
import uo.ri.util.exception.BusinessException;

public class GenerateOrder implements Command<List<OrderDto>> {
    private OrderRepository orderRepo = Factories.repository.forOrder();
    private SparePartRepository spareRepo = Factories.repository.forSparePart();
    private List<OrderDto> result = new ArrayList<>();
    private Map<String, Order> ordersByProvider = new HashMap<>();

    @Override
    public List<OrderDto> execute() throws BusinessException {
        List<SparePart> sparesNeedingOrder = spareRepo.findUnderStockNotPending();
        
        for (SparePart spare : sparesNeedingOrder) {
            Supply bestSupply = spare.getBestSupply();
            if (bestSupply == null) {
                continue; 
            }
            
            Provider provider = bestSupply.getProvider();
            String providerNif = provider.getNif();
            
            Order order = ordersByProvider.get(providerNif);
            if (order == null) {
                order = createNewOrder(provider);
                ordersByProvider.put(providerNif, order);
            }
            
            order.addSparePartFromSupply(bestSupply);
        }
        
        for (Order order : ordersByProvider.values()) {
            orderRepo.add(order);
            result.add(DtoAssembler.toDto(order));
        }
        
        return result;
    }

    private Order createNewOrder(Provider provider) {
        String code = UUID.randomUUID().toString();
        Order order = new Order(code, provider, LocalDate.now());
        return order;
    }
}
