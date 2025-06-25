package uo.ri.cws.domain;

import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import uo.ri.util.assertion.ArgumentChecks;

/**
 * This class is a Value Type, thus - no setters - hashcode and equals over all
 * attributes
 */
@Embeddable
public class Address {

    @Basic(optional = false)
    private String street;
    @Basic(optional = false)
    private String city;
    @Basic(optional = false)
    private String zipCode;

    public Address(String street, String city, String zipCode) {
        ArgumentChecks.isNotBlank(street, "Invalid street");
        ArgumentChecks.isNotBlank(city, "Invalid city");
        ArgumentChecks.isNotBlank(zipCode, "Invalid zipCode");
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    Address() {

    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        return Objects.equals(city, other.city)
            && Objects.equals(street, other.street)
            && Objects.equals(zipCode, other.zipCode);
    }

}
