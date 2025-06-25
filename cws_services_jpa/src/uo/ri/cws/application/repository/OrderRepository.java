package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Order;

public interface OrderRepository extends Repository<Order> {

    Optional<Order> findByCode(String code);

    List<Order> findByProviderNif(String nif);

    List<Order> findBySparePartCode(String code);

}
