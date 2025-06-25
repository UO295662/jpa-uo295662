package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.ProviderRepository;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ProviderJpaRepository extends BaseJpaRepository<Provider>
    implements ProviderRepository {

    @Override
    public Optional<Provider> findByNif(String nif) {
        TypedQuery<Provider> q = Jpa.getManager()
            .createNamedQuery("Provider.findByNif", Provider.class);
        q.setParameter(1, nif);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Provider> findByNameMailPhone(String name, String email,
        String phone) {
        TypedQuery<Provider> q = Jpa.getManager()
            .createNamedQuery("Provider.findByNameMailPhone", Provider.class);
        q.setParameter(1, name);
        q.setParameter(2, email);
        q.setParameter(3, phone);
        return q.getResultList();
    }

    @Override
    public List<Provider> findByName(String name) {
        TypedQuery<Provider> q = Jpa.getManager()
            .createNamedQuery("Provider.findByName", Provider.class);
        q.setParameter(1, name);
        return q.getResultList();
    }

    @Override
    public List<Provider> findBySparePartCode(String code) {
        TypedQuery<Provider> q = Jpa.getManager()
            .createNamedQuery("Provider.findBySparePartCode", Provider.class);
        q.setParameter(1, code);
        return q.getResultList();
    }

}
