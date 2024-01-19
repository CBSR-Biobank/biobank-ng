package edu.ualberta.med.biobank.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * caTissue Term - Aliquot: Pertaining to a portion of the whole; any one of two
 * or more samples of something, of the same volume or weight.
 *
 * NCI Term - Specimen: A part of a thing, or of several things, taken to
 * demonstrate or to determine the character of the whole, e.g. a substance, or
 * portion of material obtained for use in testing, examination, or study;
 * particularly, a preparation of tissue or bodily fluid taken for examination
 * or diagnosis.
 */
@Entity
@Table(name = "SPECIMEN")
public class Specimen extends DomainEntity implements HasStatus, HasComments, HasCreatedAt {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Specimen.inventoryId.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.Specimen.inventoryId.NotBlank}")
    @Column(name = "INVENTORY_ID", unique = true, nullable = false, length = 100)
    private String inventoryId;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.quantity.Digits}")
    @Column(name = "QUANTITY", precision = 10, scale = 10)
    private BigDecimal quantity;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.createdAt.NotNull}")
    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOP_SPECIMEN_ID")
    private Specimen topSpecimen = this;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.collectionEvent.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLLECTION_EVENT_ID", nullable = false)
    private CollectionEvent collectionEvent;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.currentCenter.NotNull}")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CURRENT_CENTER_ID", nullable = false)
    private Center currentCenter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "specimen")
    private Set<DispatchSpecimen> dispatchSpecimens = new HashSet<DispatchSpecimen>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGINAL_COLLECTION_EVENT_ID")
    private CollectionEvent originalCollectionEvent;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.specimenType.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_TYPE_ID", nullable = false)
    private SpecimenType specimenType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "specimen")
    private SpecimenPosition specimenPosition;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentSpecimen", cascade = CascadeType.PERSIST )
    private Set<Specimen> childSpecimens = new HashSet<Specimen>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SPECIMEN_COMMENT",
    joinColumns = { @JoinColumn(name = "SPECIMEN_ID", nullable = false) },
    inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_ID", updatable = false)
    private Set<RequestSpecimen> requestSpecimens = new HashSet<RequestSpecimen>(0);

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.originInfo.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGIN_INFO_ID", nullable = false)
    private OriginInfo originInfo;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Specimen.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status activityStatus = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESSING_EVENT_ID")
    private ProcessingEvent processingEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_SPECIMEN_ID")
    private Specimen parentSpecimen;

    @Column(name = "PLATE_ERRORS", columnDefinition = "TEXT")
    private String plateErrors;

    @Column(name = "SAMPLE_ERRORS", columnDefinition = "TEXT")
    private String sampleErrors;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "specimen")
    private Dna dna;

    public String getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Specimen getTopSpecimen() {
        return this.topSpecimen;
    }

    public void setTopSpecimen(Specimen topSpecimen) {
        this.topSpecimen = topSpecimen;
    }

    public CollectionEvent getCollectionEvent() {
        return this.collectionEvent;
    }

    public void setCollectionEvent(CollectionEvent collectionEvent) {
        this.collectionEvent = collectionEvent;
    }

    public Center getCurrentCenter() {
        return this.currentCenter;
    }

    public void setCurrentCenter(Center currentCenter) {
        this.currentCenter = currentCenter;
    }

    public Set<DispatchSpecimen> getDispatchSpecimens() {
        return this.dispatchSpecimens;
    }

    public void setDispatchSpecimens(Set<DispatchSpecimen> dispatchSpecimens) {
        this.dispatchSpecimens = dispatchSpecimens;
    }

    public CollectionEvent getOriginalCollectionEvent() {
        return this.originalCollectionEvent;
    }

    public void setOriginalCollectionEvent(
        CollectionEvent originalCollectionEvent) {
        this.originalCollectionEvent = originalCollectionEvent;
    }

    public SpecimenType getSpecimenType() {
        return this.specimenType;
    }

    public void setSpecimenType(SpecimenType specimenType) {
        this.specimenType = specimenType;
    }

    public SpecimenPosition getSpecimenPosition() {
        return this.specimenPosition;
    }

    public void setSpecimenPosition(SpecimenPosition specimenPosition) {
        this.specimenPosition = specimenPosition;
    }

    public Set<Specimen> getChildSpecimens() {
        return this.childSpecimens;
    }

    public void setChildSpecimens(Set<Specimen> childSpecimens) {
        this.childSpecimens = childSpecimens;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<RequestSpecimen> getRequestSpecimens() {
        return this.requestSpecimens;
    }

    public void setRequestSpecimens(Set<RequestSpecimen> requestSpecimens) {
        this.requestSpecimens = requestSpecimens;
    }

    public OriginInfo getOriginInfo() {
        return this.originInfo;
    }

    public void setOriginInfo(OriginInfo originInfo) {
        this.originInfo = originInfo;
    }

    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
    }

    public ProcessingEvent getProcessingEvent() {
        return this.processingEvent;
    }

    public void setProcessingEvent(ProcessingEvent processingEvent) {
        this.processingEvent = processingEvent;
    }

    public Specimen getParentSpecimen() {
        return this.parentSpecimen;
    }

    public void setParentSpecimen(Specimen parentSpecimen) {
        this.parentSpecimen = parentSpecimen;
    }

    public String getPlateErrors() {
        return plateErrors;
    }

    public void setPlateErrors(String plateErrors) {
        this.plateErrors = plateErrors;
    }

    public String getSampleErrors() {
        return sampleErrors;
    }

    public void setSampleErrors(String sampleErrors) {
        this.sampleErrors = sampleErrors;
    }

    public Dna getDna() {
        return this.dna;
    }

    public void setDna(Dna dna) {
        this.dna = dna;
    }
}
