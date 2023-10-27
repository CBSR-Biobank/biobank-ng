package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * An abstract class that represents either a collection location, a research location, or
 * repository site. See \ref Clinic, \ref Site and \ref ResearchGroup.
 */
@Entity
@Table(name = "CENTER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
public class Center extends DomainEntity
    implements HasName, HasNameShort, HasComments, HasStatus, HasAddress {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Center.name.NotEmpty}")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Center.nameShort.NotEmpty}")
    @Column(name = "NAME_SHORT", unique = true, nullable = false, length = 50)
    private String nameShort;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Center.status.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    private Status status = Status.ACTIVE;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Center.address.NotNull}")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", unique = true, nullable = false)
    private Address address = new Address();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "center")
    private Set<ProcessingEvent> processingEvents = new HashSet<ProcessingEvent>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "senderCenter")
    private Set<Dispatch> srcDispatches = new HashSet<Dispatch>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiverCenter")
    private Set<Dispatch> dstDispatches = new HashSet<Dispatch>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CENTER_ID", updatable = false)
    private Set<OriginInfo> originInfos = new HashSet<OriginInfo>(0);

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "CENTER_COMMENT",
        joinColumns = { @JoinColumn(name = "CENTER_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false, updatable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNameShort() {
        return this.nameShort;
    }

    @Override
    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<ProcessingEvent> getProcessingEvents() {
        return this.processingEvents;
    }

    public void setProcessingEvents(Set<ProcessingEvent> processingEvents) {
        this.processingEvents = processingEvents;
    }

    public Set<Dispatch> getSrcDispatches() {
        return this.srcDispatches;
    }

    public void setSrcDispatches(Set<Dispatch> srcDispatches) {
        this.srcDispatches = srcDispatches;
    }

    public Set<Dispatch> getDstDispatches() {
        return this.dstDispatches;
    }

    public void setDstDispatches(Set<Dispatch> dstDispatches) {
        this.dstDispatches = dstDispatches;
    }

    // TODO: why does this cascade exist?
    public Set<OriginInfo> getOriginInfos() {
        return this.originInfos;
    }

    public void setOriginInfos(Set<OriginInfo> originInfos) {
        this.originInfos = originInfos;
    }

    @Override
    public Status getActivityStatus() {
        return this.status;
    }

    @Override
    public void setActivityStatus(Status status) {
        this.status = status;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Should only be used by the Action layer.
     *
     * @return the studies this center is associated with.
     */
    @Transient
    public Set<Study> getStudiesInternal() {
        throw new IllegalStateException("should be implemented by derived classes");
    }

}
