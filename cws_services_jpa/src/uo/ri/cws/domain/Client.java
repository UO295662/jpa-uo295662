package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TClients")
public class Client extends BaseEntity {

    @Column(unique = true)
    private String nif;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String surname;
    @Basic(optional = false)
    private String email;
    @Basic(optional = false)
    private String phone;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = ("client"))
    private Set<Vehicle> vehicles = new HashSet<>();
    @OneToMany(mappedBy = ("client"))
    private Set<PaymentMean> paymentMeans = new HashSet<>();

    Client() {

    }

    public Client(String nif, String nombre, String apellidos, String email,
        String phone, Address address) {
        ArgumentChecks.isNotBlank(nif, "Invalid nif");
        ArgumentChecks.isNotBlank(nombre, "Invalid name");
        ArgumentChecks.isNotBlank(apellidos, "Invalid surname");
        ArgumentChecks.isNotBlank(email, "Invalid email");
        ArgumentChecks.isNotBlank(phone, "Invalid phone");
        ArgumentChecks.isNotNull(address, "Invalid address");

        this.nif = nif;
        this.name = nombre;
        this.surname = apellidos;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Client(String nif, String nombre, String apellidos) {
        this(nif, nombre, apellidos, "no-email", "no-phone",
            new Address("no-street", "no-city", "no-zipcode"));
    }

    public Client(String nif) {
        this(nif, "no-name", "no-surname");
    }

    public String getNif() {
        return nif;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    @Override
    public String toString() {
        return "Client [nif=" + nif + ", name=" + name + ", surname=" + surname
            + ", email=" + email + ", phone=" + phone + ", address=" + address
            + "]";
    }

    Set<Vehicle> _getVehicles() {
        return this.vehicles;
    }

    Set<PaymentMean> _getPaymentMeans() {
        return paymentMeans;
    }

    public Set<PaymentMean> getPaymentMeans() {
        return new HashSet<>(paymentMeans);
    }

    public void setAddress(Address address2) {
        ArgumentChecks.isNotNull(address2, "Invalid address");

        address = address2;
    }

}