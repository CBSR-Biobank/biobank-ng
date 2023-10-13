package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

// TODO: test name uniqueness? Rethink design regarding localization?
@Entity
@Table(name = "SHIPPING_METHOD")
public class ShippingMethod extends DomainEntity implements HasName {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.ShippingMethod.name.NotEmpty}")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
