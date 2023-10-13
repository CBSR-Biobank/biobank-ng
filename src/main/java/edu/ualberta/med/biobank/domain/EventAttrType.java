package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

// TODO: make enum?
@Entity
@Table(name = "EVENT_ATTR_TYPE")
public class EventAttrType extends DomainEntity implements HasName {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.EventAttrType.name.NotEmpty}")
    @Column(name = "NAME", unique = true, nullable = false, length = 50)
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
