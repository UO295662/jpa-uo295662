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
@Table(name = "TProviders")
public class Provider extends BaseEntity {

    @Column(unique = true)
    private String nif;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private String phone;

    @OneToMany(mappedBy = "provider")
    private Set<Supply> supplies = new HashSet<>();
    @OneToMany(mappedBy = "provider")
    private Set<Order> orders = new HashSet<>();

    Provider() {

    }

    public Provider(String nif, String name, String email, String phone) {
        ArgumentChecks.isNotBlank(nif, "Invalid nif");
        ArgumentChecks.isNotBlank(email, "Invalid email");
        ArgumentChecks.isNotBlank(name, "Invalid name");
        ArgumentChecks.isNotBlank(phone, "Invalid phone");

        this.nif = nif;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Provider(String name) {
        this("nif", name, "no-email", "no-phone");
    }

    public String getNif() {
        return nif;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        ArgumentChecks.isNotNull(name);
        ArgumentChecks.isNotBlank(name);

        this.name = name;
    }

    public void setEmail(String email) {
        ArgumentChecks.isNotNull(email);
        ArgumentChecks.isNotBlank(email);

        this.email = email;
    }

    public void setPhone(String phone) {
        ArgumentChecks.isNotNull(phone);
        ArgumentChecks.isNotBlank(phone);

        this.phone = phone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(nif);
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
        Provider other = (Provider) obj;
        return Objects.equals(nif, other.nif);
    }

    Set<Supply> _getSupplies() {
        return this.supplies;
    }

    Set<Order> _getOrders() {
        return this.orders;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

}
