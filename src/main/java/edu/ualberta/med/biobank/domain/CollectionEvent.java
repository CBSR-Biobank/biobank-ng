package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "COLLECTION_EVENT", uniqueConstraints = @UniqueConstraint(columnNames = { "PATIENT_ID", "VISIT_NUMBER" }))
public class CollectionEvent extends DomainEntity implements HasStatus, HasComments {

    @Min(value = 1, message = "{edu.ualberta.med.biobank.domain.CollectionEvent.visitNumber.Min}")
    @NotNull(message = "{edu.ualberta.med.biobank.domain.CollectionEvent.visitNumber.NotNull}")
    @Column(name = "VISIT_NUMBER", nullable = false)
    private Integer visitNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collectionEvent")
    private Set<Specimen> allSpecimens = new HashSet<Specimen>(0);

    @NotNull(message = "{edu.ualberta.med.biobank.domain.CollectionEvent.patient.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT_ID", nullable = false)
    private Patient patient;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.CollectionEvent.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status activityStatus = Status.ACTIVE;

    @OneToMany(cascade = jakarta.persistence.CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "collectionEvent")
    private Set<EventAttr> eventAttrs = new HashSet<EventAttr>(0);

    @ManyToMany(cascade = jakarta.persistence.CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "COLLECTION_EVENT_COMMENT", joinColumns = {
            @JoinColumn(name = "COLLECTION_EVENT_ID", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originalCollectionEvent")
    private Set<Specimen> originalSpecimens = new HashSet<Specimen>(0);

    public Integer getVisitNumber() {
        return this.visitNumber;
    }

    public void setVisitNumber(Integer visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Set<Specimen> getAllSpecimens() {
        return this.allSpecimens;
    }

    public void setAllSpecimens(Set<Specimen> allSpecimens) {
        this.allSpecimens = allSpecimens;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Set<EventAttr> getEventAttrs() {
        return this.eventAttrs;
    }

    public void setEventAttrs(Set<EventAttr> eventAttrs) {
        this.eventAttrs = eventAttrs;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Specimen> getOriginalSpecimens() {
        return this.originalSpecimens;
    }

    public void setOriginalSpecimens(Set<Specimen> originalSpecimens) {
        this.originalSpecimens = originalSpecimens;
    }
}
