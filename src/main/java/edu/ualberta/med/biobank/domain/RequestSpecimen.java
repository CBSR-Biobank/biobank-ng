package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import edu.ualberta.med.biobank.domain.type.RequestSpecimenState;

@Entity
@Table(name = "REQUEST_SPECIMEN")
public class RequestSpecimen extends DomainEntity {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.RequestSpecimen.state.NotNull}")
    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RequestSpecimenState state = RequestSpecimenState.AVAILABLE_STATE;

    @Column(name = "CLAIMED_BY", length = 50)
    private String claimedBy;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.RequestSpecimen.specimen.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_ID", nullable = false)
    private Specimen specimen;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.RequestSpecimen.request.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID", nullable = false)
    private Request request;

    public RequestSpecimenState getState() {
        return this.state;
    }

    public void setState(RequestSpecimenState state) {
        this.state = state;
    }

    public String getClaimedBy() {
        return this.claimedBy;
    }

    public void setClaimedBy(String claimedBy) {
        this.claimedBy = claimedBy;
    }

    public Specimen getSpecimen() {
        return this.specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
