package uo.ri.cws.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TCharges")
public class Charge extends BaseEntity {

    @Basic(optional = false)
    private double amount = 0.0;

    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    private PaymentMean paymentMean;

    Charge() {

    }

    public Charge(Invoice invoice, PaymentMean paymentMean, double amount) {
        ArgumentChecks.isNotNull(invoice, "The invoice cant be null");
        ArgumentChecks.isNotNull(paymentMean, "The workOrder can't be null");

        this.amount = amount;

        paymentMean.pay(amount);

        Associations.Settle.link(invoice, this, paymentMean);
    }

    /**
     * Unlinks this charge and restores the accumulated to the payment mean
     * 
     * @throws IllegalStateException if the invoice is already settled
     */
    public void rewind() {
        StateChecks.isTrue(invoice.isNotSettled(),
            "Invoice is already settled");
        paymentMean.pay(-amount);
        Associations.Settle.unlink(this);
    }

    public double getAmount() {
        return amount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    Invoice _getInvoice() {
        return invoice;
    }

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    @Override
    public String toString() {
        return "Charge [amount=" + amount + ", invoice=" + invoice
            + ", paymentMean=" + paymentMean + "]";
    }

    void _setPaymentMean(PaymentMean mp) {
        this.paymentMean = mp;
    }

    PaymentMean _getPaymentMean() {
        return paymentMean;
    }
}
