package edu.ualberta.med.biobank.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * caTissue Term - Specimen Distribution: An event that results in transfer of a
 * specimen from a Repository to a Laboratory
 *
 */
@Entity
@Table(name = "REQUEST")
public class Request extends DomainEntity implements HasCreatedAt, HasAddress {

    @Column(name = "SUBMITTED")
    private Date submitted;

    // TODO: rename column to CREATED_AT?
    @NotNull(message = "{edu.ualberta.med.biobank.domain.Request.created.NotNull}")
    @Column(name = "CREATED", nullable = false)
    private Date created;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Request.address.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", nullable = false)
    private Address address = new Address();

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Request.researchGroup.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESEARCH_GROUP_ID", nullable = false)
    private ResearchGroup researchGroup;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_ID")
    private Set<Dispatch> dispatches = new HashSet<Dispatch>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "request")
    private Set<RequestSpecimen> requestSpecimens = new HashSet<RequestSpecimen>(0);

    public Date getSubmitted() {
        return this.submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    @Override
    public Date getCreatedAt() {
        return this.created;
    }

    @Override
    public void setCreatedAt(Date created) {
        this.created = created;
    }

    public Set<Dispatch> getDispatches() {
        return this.dispatches;
    }

    public void setDispatches(Set<Dispatch> dispatches) {
        this.dispatches = dispatches;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "request")
    public Set<RequestSpecimen> getRequestSpecimens() {
        return this.requestSpecimens;
    }

    public void setRequestSpecimens(Set<RequestSpecimen> requestSpecimens) {
        this.requestSpecimens = requestSpecimens;
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    public ResearchGroup getResearchGroup() {
        return this.researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }
}
