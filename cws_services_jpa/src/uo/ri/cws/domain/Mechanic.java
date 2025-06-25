package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TMechanics")
public class Mechanic extends BaseEntity {

    @Column(unique = true)
    private String nif;
    @Basic(optional = false)
    private String surname;
    @Basic(optional = false)
    private String name;

    @OneToMany(mappedBy = "mechanic")
    private Set<WorkOrder> assigned = new HashSet<>();
    @OneToMany(mappedBy = "mechanic")
    private Set<Intervention> interventions = new HashSet<>();

    Mechanic() {

    }

    public Mechanic(String nif, String name, String surname) {
        ArgumentChecks.isNotBlank(nif, "Invalid nif");
        ArgumentChecks.isNotBlank(surname, "Invalid surname");
        ArgumentChecks.isNotBlank(name, "Invalid name");

        this.nif = nif;
        this.surname = surname;
        this.name = name;
    }

    public Mechanic(String nif) {
        this(nif, "no-name", "no-surname");
    }

    public Set<WorkOrder> getAssigned() {
        return new HashSet<>(assigned);
    }

    Set<WorkOrder> _getAssigned() {
        return assigned;
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    public String getNif() {
        return nif;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Mechanic [nif=" + nif + ", surname=" + surname + ", name="
            + name + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mechanic other = (Mechanic) obj;
        return Objects.equals(nif, other.nif);
    }

    public void setName(String name) {
        ArgumentChecks.isNotNull(name, "name null");
        ArgumentChecks.isNotBlank(name, "Invalid name");

        this.name = name;
    }

    public void setSurname(String surname) {
        ArgumentChecks.isNotNull(surname, "surname null");
        ArgumentChecks.isNotBlank(surname, "Invalid surname");

        this.surname = surname;
    }

}
