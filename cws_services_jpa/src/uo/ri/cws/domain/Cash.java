package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCashes")
public class Cash extends PaymentMean {

    Cash() {

    }

    public Cash(Client client) {
        ArgumentChecks.isNotNull(client, "Invalid client");
        Associations.Hold.link(this, client);
    }

    @Override
    public boolean canPay(Double amount) {
        return amount > 0;
    }

}
