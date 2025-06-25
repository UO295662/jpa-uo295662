package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.SparePartRepository;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class SparePartJpaRepository extends BaseJpaRepository<SparePart>
    implements SparePartRepository {

    @Override
    public Optional<SparePart> findByCode(String code) {
        TypedQuery<SparePart> q = Jpa.getManager()
            .createNamedQuery("SparePart.findByCode", SparePart.class);
        q.setParameter(1, code);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<SparePart> findUnderStockNotPending() {
        TypedQuery<SparePart> q = Jpa.getManager().createNamedQuery(
            "SparePart.findUnderStockNotPending", SparePart.class);
        return q.getResultList();
    }

    @Override
    public List<SparePart> findByDescription(String desc) {
        TypedQuery<SparePart> q = Jpa.getManager()
            .createNamedQuery("SparePart.findByDescription", SparePart.class);
        q.setParameter(1, desc);
        return q.getResultList();
    }

    @Override
    public List<SparePart> findUnderStock() {
        TypedQuery<SparePart> q = Jpa.getManager()
            .createNamedQuery("SparePart.findUnderStock", SparePart.class);
        return q.getResultList();
    }

    @Override
    public List<SparePart> findOverStock() {
        TypedQuery<SparePart> q = Jpa.getManager()
            .createNamedQuery("SparePart.findOverStock", SparePart.class);
        return q.getResultList();
    }

}
