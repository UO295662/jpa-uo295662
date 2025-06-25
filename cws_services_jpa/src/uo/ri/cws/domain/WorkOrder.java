package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;
import uo.ri.util.math.Round;

@Entity
@Table(name = "TWorkOrders", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "vehicle_id", "date" }) })
public class WorkOrder extends BaseEntity {
    public enum WorkOrderState {
        OPEN, ASSIGNED, FINISHED, INVOICED
    }

    @Column(unique = true)
    private LocalDateTime date;
    @Basic(optional = false)
    private String description;
    @Basic(optional = false)
    private double amount = 0.0;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private WorkOrderState state = WorkOrderState.OPEN;

    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private Invoice invoice;
    @OneToMany(mappedBy = "workOrder")
    private Set<Intervention> interventions = new HashSet<>();

    WorkOrder() {

    }

    public WorkOrder(Vehicle vehicle, String desc) {
        this(vehicle, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS), desc);
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {
        ArgumentChecks.isNotBlank(description, "Invalid description");
        ArgumentChecks.isNotNull(date, "Null date");
        ArgumentChecks.isNotNull(vehicle, "Null vehicle");

        this.date = date.truncatedTo(ChronoUnit.MILLIS);
        this.description = description;

        Associations.Fix.link(vehicle, this);
    }

    public WorkOrder(Vehicle vehicle) {
        this(vehicle, "no-description");
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime now) {
        this(vehicle, now, "no-description");
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isFinished() {
        return WorkOrderState.FINISHED.equals(state);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    @Override
    public String toString() {
        return "WorkOrder [date=" + date + ", description=" + description
            + ", amount=" + amount + ", state=" + state + ", vehicle=" + vehicle
            + "]";
    }

    /**
     * Changes it to INVOICED state given the right conditions This method is
     * called from Invoice.addWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not FINISHED, or -
     *                               The work order is not linked with the
     *                               invoice
     */
    public void markAsInvoiced() {
        StateChecks.isTrue(isFinished(), "El workorder no esta FINISHED");
        StateChecks.isTrue(this.invoice != null,
            "The workorder is not linked with the invoice");

        this.state = WorkOrderState.INVOICED;
    }

    /**
     * Changes it to FINISHED state given the right conditions and computes the
     * amount
     *
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state, or - The work order is not linked
     *                               with a mechanic
     */
    public void markAsFinished() {
        StateChecks.isTrue(isAssigned());
        StateChecks.isTrue(this.mechanic != null);

        this.state = WorkOrderState.FINISHED;
        this.amount = computeAmount();
    }

    private double computeAmount() {
        double localAmount = 0.0;

        for (Intervention intervention : interventions) {
            localAmount += intervention.getAmount();
        }

        return Round.twoCents(localAmount);
    }

    public boolean isAssigned() {
        return state.equals(WorkOrderState.ASSIGNED);
    }

    /**
     * Changes it back to FINISHED state given the right conditions This method
     * is called from Invoice.removeWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not INVOICED, or -
     *                               The work order is still linked with the
     *                               invoice
     */
    public void markBackToFinished() {
        StateChecks.isTrue(isInvoiced());
        StateChecks.isTrue(this.mechanic != null);

        this.state = WorkOrderState.FINISHED;
    }

    /**
     * Links (assigns) the work order to a mechanic and then changes its state
     * to ASSIGNED
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in OPEN state,
     *                               or - The work order is already linked with
     *                               another mechanic
     */
    public void assignTo(Mechanic mechanic) {
        StateChecks.isTrue(isOpen());
        StateChecks.isTrue(this.mechanic == null);

        Associations.Assign.link(mechanic, this);

        this.state = WorkOrderState.ASSIGNED;
    }

    private boolean isOpen() {
        return this.state.equals(WorkOrderState.OPEN);
    }

    /**
     * Unlinks (deassigns) the work order and the mechanic and then changes its
     * state back to OPEN
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state
     */
    public void desassign() {
        StateChecks.isTrue(isAssigned());

        Associations.Assign.unlink(mechanic, this);
        this.state = WorkOrderState.OPEN;
    }

    /**
     * In order to assign a work order to another mechanic is first have to be
     * moved back to OPEN state and unlinked from the previous mechanic.
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in FINISHED
     *                               state
     */
    public void reopen() {
        StateChecks.isTrue(isFinished());

        this.state = WorkOrderState.OPEN;
        Associations.Assign.unlink(mechanic, this);
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    void _setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public WorkOrderState getState() {
        return this.state;
    }

    public boolean isInvoiced() {
        return this.state.equals(WorkOrderState.INVOICED);
    }

}
