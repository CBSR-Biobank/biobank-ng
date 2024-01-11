package edu.ualberta.med.biobank.domain;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Participant: An individual from which a biospecimen is collected.
 *
 * A person who receives medical attention, care, or treatment, or who is registered with medical professional
 * or institution with the purpose to receive medical care when necessary.
 *
 * Note: since this application will be used for inventory control of non human
 * participants, this class should be renamed to Participant.
 */
@Entity
@Table(name = "PATIENT")
public class Patient extends DomainEntity implements HasCreatedAt, HasComments {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Patient.pnumber.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.Patient.pnumber.NotBlank}")
    @Column(name = "PNUMBER", unique = true, nullable = false, length = 100)
    private String pnumber;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Patient.createdAt.NotNull}")
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Patient.study.NotNull}")
    @ManyToOne
    @JoinColumn(name = "STUDY_ID", nullable = false)
    private Study study;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private Set<CollectionEvent> collectionEvents = new HashSet<CollectionEvent>(0);

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PATIENT_COMMENT",
        joinColumns = { @JoinColumn(name = "PATIENT_ID", nullable = false) },
        inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    public String getPnumber() {
        return this.pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
    public Set<CollectionEvent> getCollectionEvents() {
        return this.collectionEvents;
    }

    public void setCollectionEvents(Set<CollectionEvent> collectionEvents) {
        this.collectionEvents = collectionEvents;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
