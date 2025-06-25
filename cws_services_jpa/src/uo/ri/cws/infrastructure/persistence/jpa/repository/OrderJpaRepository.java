package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.OrderRepository;
import uo.ri.cws.domain.Order;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class OrderJpaRepository extends BaseJpaRepository<Order>
    implements OrderRepository {

    @Override
    public Optional<Order> findByCode(String code) {
        TypedQuery<Order> q = Jpa.getManager()
            .createNamedQuery("Order.findByCode", Order.class);
        q.setParameter(1, code);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Order> findByProviderNif(String nif) {
        TypedQuery<Order> q = Jpa.getManager()
            .createNamedQuery("Order.findByProviderNif", Order.class);
        q.setParameter(1, nif);
        return q.getResultList();
    }

    @Override
    public List<Order> findBySparePartCode(String code) {
        TypedQuery<Order> q = Jpa.getManager()
            .createNamedQuery("Order.findBySparePartCode", Order.class);
        q.setParameter(1, code);
        return q.getResultList();
    }

}
