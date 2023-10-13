package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "GLOBAL_EVENT_ATTR")
public class GlobalEventAttr extends DomainEntity {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.GlobalEventAttr.label.NotEmpty}")
    @Column(name = "LABEL", unique = true, nullable = false, length = 50)
    private String label;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.GlobalEventAttr.eventAttrType.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ATTR_TYPE_ID", nullable = false)
    private EventAttrType eventAttrType;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EventAttrType getEventAttrType() {
        return this.eventAttrType;
    }

    public void setEventAttrType(EventAttrType eventAttrType) {
        this.eventAttrType = eventAttrType;
    }
}
