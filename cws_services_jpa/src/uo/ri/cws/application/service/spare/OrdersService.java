package uo.ri.cws.application.service.spare;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.util.exception.BusinessException;

public interface OrdersService {

    /**
     * Generates orders following the wording
     * 
     * @return the list of new order dto generated
     * @throws BusinessException DOES NOT
     */
    List<OrderDto> generateOrders() throws BusinessException;

    /**
     * Returns all the orders for the provider identified by its nif
     * 
     * @param nif
     * @return a list with orders or empty if there is no order from the
     *         provider
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the nif is null
     */
    List<OrderDto> findByProviderNif(String nif) throws BusinessException;

    /**
     * @param code
     * @return the order identified by the code or Optional.empty() if does not
     *         exist
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the code is null
     */
    Optional<OrderDto> findByCode(String code) throws BusinessException;

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
    OrderDto receive(String code) throws BusinessException;

    public static class OrderDto {
        public String id;
        public long version;

        public String code;
        public LocalDate orderedDate;
        public LocalDate receptionDate;
        public double amount;
        public String state;

        public OrderedProviderDto provider = new OrderedProviderDto();
        public List<OrderLineDto> lines = new ArrayList<>();

        public static class OrderLineDto {
            public OrderedSpareDto sparePart = new OrderedSpareDto();
            public double price;
            public int quantity;
        }

        public static class OrderedSpareDto {
            public String id;
            public String code;
            public String description;
        }

        public static class OrderedProviderDto {
            public String id;
            public String nif;
            public String name;
        }

    }

}
