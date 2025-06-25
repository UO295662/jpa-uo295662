package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVehicleTypes")
public class VehicleType extends BaseEntity {

    @Column(unique = true)
    private String name;
    @Basic(optional = false)
    private double pricePerHour;

    @OneToMany(mappedBy = "vehicleType")
    private Set<Vehicle> vehicles = new HashSet<>();

    VehicleType() {

    }

    public VehicleType(String name, double pricePerHour) {
        ArgumentChecks.isNotBlank(name, "Invalid name");
        ArgumentChecks.isTrue(pricePerHour >= 0, "Invalid pricePerHour");

        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public VehicleType(String name) {
        this(name, 100.0);
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    public String getName() {
        return name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    @Override
    public String toString() {
        return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour
            + "]";
    }

}
