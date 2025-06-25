package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class InvoiceJpaRepository extends BaseJpaRepository<Invoice>
    implements InvoiceRepository {

    @Override
    public Optional<Invoice> findByNumber(Long number) {
        TypedQuery<Invoice> q = Jpa.getManager()
            .createNamedQuery("Invoice.findByNumber", Invoice.class);
        q.setParameter(1, number);
        return q.getResultStream().findFirst();
    }

    @Override
    public Long getNextInvoiceNumber() {
        TypedQuery<Long> q = Jpa.getManager()
            .createNamedQuery("Invoice.getNextInvoiceNumber", Long.class);

        Long numero = null;

        List<Long> resultado = q.getResultList();

        if (!resultado.isEmpty()) {
            numero = resultado.get(0);
        }

        return numero;
    }

}
