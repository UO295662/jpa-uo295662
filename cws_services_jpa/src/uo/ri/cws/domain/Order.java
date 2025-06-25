package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TOrders")
public class Order extends BaseEntity {
    @Column(unique = true)
    private String code;
    @Basic(optional = false)
    private LocalDate orderedDate;
    @Basic(optional = false)
    private LocalDate receptionDate;
    @Basic(optional = false)
    private double amount;
    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.PENDING;

    public enum OrderState {
        PENDING, RECEIVED
    }

    @ElementCollection
    @CollectionTable(name = "TOrderLines", joinColumns = @JoinColumn(name = "order_id"))
    private Set<OrderLine> orderLines = new HashSet<>();
    @ManyToOne
    private Provider provider;

    Order() {
    }

    public Order(String code) {
        this(code, new Provider("no-name"), LocalDate.now());
    }

    public Order(String code, Provider provider, LocalDate date) {
        ArgumentChecks.isNotNull(provider, "Invalid provider");
        ArgumentChecks.isNotBlank(code, "Invalid code");
        this.code = code;
        this.orderedDate = date;
        Associations.Deliver.link(provider, this);
    }

    public String getCode() {
        return code;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    public LocalDate getReceptionDate() {
        return receptionDate;
    }

    public OrderState getState() {
        return state;
    }

    public double getAmount() {
        return amount;
    }

    public Set<OrderLine> getOrderLines() {
        return new HashSet<>(this.orderLines);
    }

    public boolean isPending() {
        return this.state.equals(OrderState.PENDING);
    }

    public boolean isReceived() {
        return this.state.equals(OrderState.RECEIVED);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(code);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        return Objects.equals(code, other.code);
    }

    public void receive() {
        StateChecks.isTrue(isPending(), "Order already received");

        for (OrderLine line : orderLines) {
            line.receive();
        }

        this.state = OrderState.RECEIVED;
        this.receptionDate = LocalDate.now();
    }

    public void addSparePartFromSupply(Supply supply) {
        ArgumentChecks.isNotNull(supply);

        SparePart sparePart = supply.getSparePart();

        for (OrderLine ol : orderLines) {
            SparePart sp = ol.getSparePart();
            if (sp.equals(sparePart)) {
                throw new IllegalStateException(
                    "Spare part already added to the order");
            }
        }

        if (sparePart.getStock() == sparePart.getMinStock()) {
            return;
        }

        OrderLine orderLine = new OrderLine(sparePart, supply.getPrice());
        orderLines.add(orderLine);

        updateAmount();
    }

    private void updateAmount() {
        this.amount = 0;
        for (OrderLine ol : this.orderLines) {
            this.amount += ol.getAmount();
        }
    }

    public void removeSparePart(SparePart sp) {
        ArgumentChecks.isNotNull(sp, "sparepart null");

        OrderLine toRemove = null;

        for (OrderLine ol : orderLines) {
            if (ol.getSparePart().equals(sp)) {
                toRemove = ol;
                break;
            }
        }

        if (toRemove != null) {
            orderLines.remove(toRemove);
        }

        updateAmount();
    }

    void _setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setReceptionDate(LocalDate now) {
        ArgumentChecks.isNotNull(now);

        this.receptionDate = now;
    }

    public void setState(OrderState received) {
        ArgumentChecks.isNotNull(received);

        this.state = received;
    }

}
