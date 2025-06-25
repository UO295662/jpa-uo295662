package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSupplies")
public class Supply extends BaseEntity {

    private double price;
    private int deliveryTerm;

    @ManyToOne
    private Provider provider;
    @ManyToOne
    private SparePart sparePart;

    Supply() {

    }

    public Supply(Provider p, SparePart sp, double price, int deliveryTerm) {
        ArgumentChecks.isNotNull(p, "Provider null");
        ArgumentChecks.isNotNull(sp, "Sparepart null");
        ArgumentChecks.isTrue(price > 0, "precio invalido");
        ArgumentChecks.isTrue(deliveryTerm > 0, "deliveryTerm invalido");

        this.deliveryTerm = deliveryTerm;
        this.price = price;

        this.provider = p;
        this.sparePart = sp;
    }

    public Provider getProvider() {
        return provider;
    }

    public SparePart getSparePart() {
        return sparePart;
    }

    public double getPrice() {
        return price;
    }

    public int getDeliveryTerm() {
        return deliveryTerm;
    }

    public void setPrice(double price) {
        ArgumentChecks.isTrue(price >= 0, "precio invalido");

        this.price = price;
    }

    public void setDeliveryTerm(int deliveryTerm) {
        ArgumentChecks.isTrue(deliveryTerm >= 0, "deliveryTerm invalido");

        this.deliveryTerm = deliveryTerm;
    }

}
