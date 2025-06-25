package uo.ri.cws.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSubstitutions", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "sparePart_id", "intervention_id" }) })
public class Substitution extends BaseEntity {

    @Column(unique = true)
    private int quantity;

    @ManyToOne
    private SparePart sparePart;
    @ManyToOne
    private Intervention intervention;

    Substitution() {
    }

    public Substitution(SparePart sparePart, Intervention intervention,
        int quantity) {
        ArgumentChecks.isNotNull(sparePart, "Null sparePart");
        ArgumentChecks.isNotNull(intervention, "Null intervention");
        ArgumentChecks.isTrue(quantity > 0, "Invalid quantity");

        Associations.Substitute.link(sparePart, this, intervention);

        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Substitution [quantity=" + quantity + "]";
    }

    public int getQuantity() {
        return quantity;
    }

    public SparePart getSparePart() {
        return sparePart;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    void _setSparePart(SparePart sparePart) {
        this.sparePart = sparePart;
    }

    void _setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public double getAmount() {
        return quantity * sparePart.getPrice();
    }

}
