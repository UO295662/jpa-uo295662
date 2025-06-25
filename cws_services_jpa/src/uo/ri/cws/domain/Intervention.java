package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TInterventions", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "workOrder_id", "mechanic_id",
        "date" }) })
public class Intervention extends BaseEntity {

    @Basic(optional = false)
    private LocalDateTime date;
    @Basic(optional = false)
    private int minutes;

    @ManyToOne
    private WorkOrder workOrder;
    @ManyToOne
    private Mechanic mechanic;
    @OneToMany(mappedBy = "intervention")
    private Set<Substitution> substitutions = new HashSet<>();

    Intervention() {

    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder,
        LocalDateTime date, int minutes) {
        ArgumentChecks.isNotNull(mechanic, "invalid mechanic");
        ArgumentChecks.isNotNull(workOrder, "invalid workOrder");
        ArgumentChecks.isNotNull(date, "invalid date");
        ArgumentChecks.isTrue(minutes >= 0, "Invalid minutes");

        this.date = date.truncatedTo(ChronoUnit.MILLIS);
        this.minutes = minutes;

        Associations.Intervene.link(workOrder, this, mechanic);
    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutos) {
        this(mechanic, workOrder, LocalDateTime.now(), minutos);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getMinutes() {
        return minutes;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    @Override
    public String toString() {
        return "Intervention [date=" + date + ", minutes=" + minutes
            + ", workOrder=" + workOrder + ", mechanic=" + mechanic + "]";
    }

    void _setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Set<Substitution> getSubstitutions() {
        return new HashSet<>(substitutions);
    }

    Set<Substitution> _getSubstitutions() {
        return this.substitutions;
    }

    public Double getAmount() {
        double cost = (this.minutes / 60.0)
            * this.workOrder.getVehicle().getVehicleType().getPricePerHour();
        double sparePart = 0;
        for (Substitution substitution : substitutions) {
            sparePart += substitution.getAmount();
        }
        return cost + sparePart;
    }

}
