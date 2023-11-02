package edu.ualberta.med.biobank.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ualberta.med.biobank.domain.type.Person;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PROCESSING_EVENT")
public class ProcessingEvent extends DomainEntity implements HasCreatedAt, HasStatus, HasComments {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.ProcessingEvent.worksheet.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.ProcessingEvent.worksheet.NotBlank}")
    @Column(name = "WORKSHEET", length = 150, unique = true)
    private String worksheet;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ProcessingEvent.createdAt.NotNull}")
    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ProcessingEvent.center.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CENTER_ID", nullable = false)
    private Center center;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processingEvent", cascade = jakarta.persistence.CascadeType.PERSIST)
    private Set<Specimen> specimens = new HashSet<Specimen>(0);

    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "PROCESSED_BY")),
            @AttributeOverride(name = "user", column = @Column(name = "PROCESSED_BY_USER_ID"))
    })
    @Valid
    @Embedded
    private Person processedBy;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ProcessingEvent.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status activityStatus = Status.ACTIVE;

    @ManyToMany(cascade = jakarta.persistence.CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "PROCESSING_EVENT_COMMENT", joinColumns = @JoinColumn(name = "PROCESSING_EVENT_ID", nullable = false, updatable = false), inverseJoinColumns = @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false, updatable = false))
    private Set<Comment> comments = new HashSet<Comment>(0);

    public String getWorksheet() {
        return this.worksheet;
    }

    public void setWorksheet(String worksheet) {
        this.worksheet = worksheet;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Center getCenter() {
        return this.center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Set<Specimen> getSpecimens() {
        return this.specimens;
    }

    public void setSpecimens(Set<Specimen> specimens) {
        this.specimens = specimens;
    }

    public Person getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Person processedBy) {
        this.processedBy = processedBy;
    }

    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
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
