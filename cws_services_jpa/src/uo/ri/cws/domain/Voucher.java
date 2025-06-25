package uo.ri.cws.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVouchers")
public class Voucher extends PaymentMean {

    @Column(unique = true)
    private String code;

    @Basic(optional = false)
    private double available = 0.0;
    @Basic(optional = false)
    private String description;

    Voucher() {

    }

    public Voucher(String code, double available, String description) {
        this(code, description, available);
        ArgumentChecks.isNotBlank(code, "Invalid code");
        ArgumentChecks.isNotBlank(description, "Invalid description");
        ArgumentChecks.isTrue(available >= 0, "Invalid available");

        this.code = code;
        this.available = available;
        this.description = description;
    }

    public Voucher(String code, String description, double available) {
        ArgumentChecks.isNotBlank(code, "Invalid code");
        ArgumentChecks.isNotBlank(description, "Invalid description");
        ArgumentChecks.isTrue(available >= 0, "Invalid available");

        this.code = code;
        this.available = available;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Voucher [code=" + code + ", available=" + available
            + ", description=" + description + "]";
    }

    public String getCode() {
        return code;
    }

    public double getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Augments the accumulated (super.pay(amount) ) and decrements the
     * available
     * 
     * @throws IllegalStateException if not enough available to pay
     */
    @Override
    public void pay(double amount) {
        if (available - amount < 0) {
            throw new IllegalStateException();
        }
        super.pay(amount);
        this.available -= amount;
    }

    @Override
    public boolean canPay(Double amount) {
        return available - amount >= 0;
    }

}
