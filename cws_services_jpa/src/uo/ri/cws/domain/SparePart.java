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
@Table(name = "TSpareParts")
public class SparePart extends BaseEntity {

    @Column(unique = true)
    private String code;
    @Basic(optional = false)
    private String description;
    @Basic(optional = false)
    private double price;
    @Basic(optional = false)
    private int stock;
    @Basic(optional = false)
    private int minStock;
    @Basic(optional = false)
    private int maxStock;

    @OneToMany(mappedBy = "sparePart")
    private Set<Substitution> substitutions = new HashSet<>();
    @OneToMany(mappedBy = "sparePart")
    private Set<Supply> supplies = new HashSet<>();

    SparePart() {

    }

    public SparePart(String code, String description, double price) {
        this(code, description, price, 0, 0, 0);
    }

    public SparePart(String code) {
        this(code, "no-description", 100.0);
    }

    public SparePart(String code, String description, double price, int stock,
        int minStock, int maxStock) {
        checkConstructorArguments(code, description, price, stock, minStock,
            maxStock);

        this.code = code;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.maxStock = maxStock;
    }

    private void checkConstructorArguments(String code, String description,
        double price, int stock, int minStock, int maxStock) {
        ArgumentChecks.isNotBlank(code, "Invalid code");
        ArgumentChecks.isNotBlank(description, "Invalid description");
        ArgumentChecks.isNotBlank(code, "Invalid code");
        ArgumentChecks.isTrue(price >= 0, "Invalid price");
        ArgumentChecks.isTrue(minStock >= 0, "Invalid min stock");
        ArgumentChecks.isTrue(stock >= 0, "Invalid stock");
        ArgumentChecks.isTrue(maxStock >= 0, "Invalid max stock");
    }

    public boolean isUnderStock() {
        return stock < minStock;
    }

    public void updatePriceAndStock(double newPrice, int newStock) {
        ArgumentChecks.isTrue(newPrice > 0,
            "El nuevo precio no puede ser negativo");
        ArgumentChecks.isTrue(newStock > 0,
            "El nuevo stock no puede ser cero o negativo");

        int updatedStock = this.stock + newStock;

        this.price = (this.stock * this.price + newStock * newPrice * 1.2)
            / updatedStock;
        this.stock = updatedStock;
    }

    public Set<Substitution> getSubstitutions() {
        return new HashSet<>(substitutions);
    }

    Set<Substitution> _getSubstitutions() {
        return substitutions;
    }

    public Set<Supply> getSupplies() {
        return new HashSet<>(supplies);
    }

    Set<Supply> _getSupplies() {
        return supplies;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public int getTotalUnitsSold() {
        int totalUnitsSold = 0;
        for (Substitution substitution : substitutions) {
            totalUnitsSold += substitution.getQuantity();
        }
        return totalUnitsSold;
    }

    public Integer getQuantityToOrder() {
        if (stock < minStock) {
            return maxStock - stock;
        }
        return 0;
    }

    public void setMinStock(int minStock) {
        ArgumentChecks.isTrue(minStock > 0, "stock minimo invalido");

        this.minStock = minStock;
    }

    public void setMaxStock(int maxStock) {
        ArgumentChecks.isTrue(maxStock > 0, "stock maximo invalido");

        this.maxStock = maxStock;
    }

    public void setStock(int stock) {
        ArgumentChecks.isTrue(stock >= 0, "stock invalido");

        this.stock = stock;
    }

    public void setPrice(double newPrice) {
        ArgumentChecks.isTrue(newPrice >= 0, "newPrice invalido");

        this.price = newPrice;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public Supply getBestSupply() {
        if (supplies == null || supplies.isEmpty()) {
            return null;
        }
        Supply cheapestSupply = null;
        for (Supply supply : supplies) {
            if (cheapestSupply == null) {
                cheapestSupply = supply;
            } else if (supply.getPrice() < cheapestSupply.getPrice()) {
                cheapestSupply = supply;
            } else if (supply.getPrice() == cheapestSupply.getPrice()) {
                if (supply.getDeliveryTerm() < cheapestSupply
                    .getDeliveryTerm()) {
                    cheapestSupply = supply;
                } else if (supply.getDeliveryTerm() == cheapestSupply
                    .getDeliveryTerm()) {
                    if (supply.getProvider().getNif()
                        .compareTo(cheapestSupply.getProvider().getNif()) < 0) {
                        cheapestSupply = supply;
                    }
                }
            }
        }
        return cheapestSupply;
    }

}
