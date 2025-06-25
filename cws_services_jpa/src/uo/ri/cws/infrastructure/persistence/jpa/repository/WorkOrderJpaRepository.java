package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class WorkOrderJpaRepository extends BaseJpaRepository<WorkOrder>
    implements WorkOrderRepository {

    @Override
    public List<WorkOrder> findByIds(List<String> idsAveria) {
        return Jpa.getManager()
            .createNamedQuery("WorkOrder.findByIds", WorkOrder.class)
            .setParameter(1, idsAveria).getResultList();
    }

    @Override
    public List<WorkOrder> findByClientNif(String nif) {
        return new ArrayList<>();
    }

    @Override
    public List<WorkOrder> findByPlateNumber(String plate) {
        return new ArrayList<>();
    }

    @Override
    public List<WorkOrder> findNotInvoicedByClientNif(String nif) {
        TypedQuery<WorkOrder> q = Jpa.getManager().createNamedQuery(
            "WorkOrder.findNotInvoicedWorkOrdersByClientNif", WorkOrder.class);
        q.setParameter(1, nif);
        return q.getResultList();
    }

}
