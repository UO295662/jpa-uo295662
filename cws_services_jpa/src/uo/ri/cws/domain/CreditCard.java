package uo.ri.cws.domain;

import java.time.LocalDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TCreditCards")
public class CreditCard extends PaymentMean {

    @Column(unique = true)
    private String number;
    @Basic(optional = false)
    private String type;
    @Basic(optional = false)
    private LocalDate validThru;

    CreditCard() {

    }

    public CreditCard(String number, String type, LocalDate validThru) {
        ArgumentChecks.isNotBlank(number, "Invalid number");
        ArgumentChecks.isNotBlank(type, "Invalid type");
        ArgumentChecks.isNotNull(validThru, "Invalid date");

        ArgumentChecks.isNotEmpty(number, "Number blank");
        ArgumentChecks.isNotEmpty(type, "type blank");

        this.number = number;
        this.type = type;
        this.validThru = validThru;
    }

    @Override
    public void pay(double amount) {
        isExpired();
        super.pay(amount);
    }

    public void isExpired() {
        StateChecks.isTrue(!getValidThru().isBefore(LocalDate.now()),
            "Expired");
    }

    @Override
    public String toString() {
        return "CreditCard [number=" + number + ", type=" + type
            + ", validThru=" + validThru + "]";
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public LocalDate getValidThru() {
        return validThru;
    }

    @Override
    public boolean canPay(Double amount) {
        return true;
    }

}
