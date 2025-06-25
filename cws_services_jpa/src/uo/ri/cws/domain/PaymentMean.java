package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TPaymentMeans")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMean extends BaseEntity {

    @Basic(optional = false)
    private double accumulated = 0.0;

    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "paymentMean")
    private Set<Charge> charges = new HashSet<>();

    PaymentMean() {

    }

    public abstract boolean canPay(Double amount);

    public void pay(double importe) {
        this.accumulated += importe;
    }

    void _setClient(Client client) {
        this.client = client;
    }

    public Set<Charge> getCharges() {
        return new HashSet<>(charges);
    }

    Set<Charge> _getCharges() {
        return charges;
    }

    public double getAccumulated() {
        return accumulated;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "PaymentMean [accumulated=" + accumulated + ", client=" + client
            + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(client);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PaymentMean other = (PaymentMean) obj;
        return Objects.equals(client, other.client);
    }

}
