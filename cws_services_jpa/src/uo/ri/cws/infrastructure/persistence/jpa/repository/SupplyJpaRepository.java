package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.SupplyRepository;
import uo.ri.cws.domain.Supply;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class SupplyJpaRepository extends BaseJpaRepository<Supply>
    implements SupplyRepository {

    @Override
    public Optional<Supply> findByNifAndCode(String nif, String code) {
        TypedQuery<Supply> q = Jpa.getManager()
            .createNamedQuery("Supply.findByNifAndCode", Supply.class);
        q.setParameter(1, nif);
        q.setParameter(2, code);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Supply> findByProviderNif(String nif) {
        TypedQuery<Supply> q = Jpa.getManager()
            .createNamedQuery("Supply.findByProviderNif", Supply.class);
        q.setParameter(1, nif);
        return q.getResultList();
    }

    @Override
    public List<Supply> findBySparePartCode(String code) {
        TypedQuery<Supply> q = Jpa.getManager()
            .createNamedQuery("Supply.findBySparePartCode", Supply.class);
        q.setParameter(1, code);
        return q.getResultList();
    }

}
