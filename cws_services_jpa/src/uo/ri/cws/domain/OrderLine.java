package uo.ri.cws.domain;

import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Embeddable
public class OrderLine {

    @Basic
    private double price;
    @Basic
    private int quantity;

    @ManyToOne
    private SparePart sparePart;

    OrderLine() {

    }

    public OrderLine(double price, int quantity, SparePart sparePart) {
        ArgumentChecks.isNotNull(sparePart, "sparePart cannot be null");
        ArgumentChecks.isTrue(price >= 0, "invalid price");
        ArgumentChecks.isTrue(quantity >= 0, "Quantity must be positive");
        ArgumentChecks.isFalse(sparePart.getStock() >= sparePart.getMaxStock(),
            "Cannot create a orderline for a spare part at max stock");

        this.price = price;
        this.quantity = quantity;

        this.sparePart = sparePart;
    }

    public OrderLine(SparePart sparePart, double price) {
        this(price, sparePart.getMaxStock() - sparePart.getStock(), sparePart);
    }

    public void receive() {
        ArgumentChecks.isTrue(this.quantity > 0, "cantidad invalida");

        StateChecks.isNotNull(sparePart);

        sparePart.updatePriceAndStock(price, quantity);
    }

    public double getPrice() {
        return this.price;
    }

    public SparePart getSparePart() {
        return this.sparePart;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getAmount() {
        return this.price * this.quantity;
    }

    public void setQuantity(int stock) {
        ArgumentChecks.isTrue(stock > 0,
            "la cantidad debe de ser mayor que cero");
    }

    public void setPrice(double nuevoPrecio) {
        ArgumentChecks.isTrue(nuevoPrecio >= 0);

        this.price = nuevoPrecio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity, sparePart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderLine other = (OrderLine) obj;
        return Double.doubleToLongBits(price) == Double
            .doubleToLongBits(other.price) && quantity == other.quantity
            && Objects.equals(sparePart, other.sparePart);
    }
}