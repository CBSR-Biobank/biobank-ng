package edu.ualberta.med.biobank.domain;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

/**
 * The specimens, derived from source specimens, that are collected for a study.
 *
 * A study can be configured to have as many aliquoted specimens as are
 * required. The aliquoted specimen states the specimen types collected by a
 * study, the number of tubes and the required volume in each tube.
 */
@Entity
@Table(name = "ALIQUOTED_SPECIMEN")
public class AliquotedSpecimen extends DomainEntity
    implements HasStatus {

    @NotNull(message = "{edu.ualberta.med.biobank.model.AliquotedSpecimen.specimenType.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_TYPE_ID", nullable = false)
    private SpecimenType specimenType;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.model.AliquotedSpecimen.volume.Digits}")
    @Column(name = "VOLUME", precision = 10, scale = 10)
    private BigDecimal volume;

    @NotNull(message = "{edu.ualberta.med.biobank.model.AliquotedSpecimen.study.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID", nullable = false)
    private Study study;

    @NotNull(message = "{edu.ualberta.med.biobank.model.AliquotedSpecimen.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status activityStatus = Status.ACTIVE;

    /**
     * @brief The number of aliquoted tubes to be collected of this specimen
     *        type.
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @brief The volume to be collected in each tube.
     */
    public BigDecimal getVolume() {
        return this.volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * @brief The specimen type that has to be collected for the study.
     */
    public SpecimenType getSpecimenType() {
        return this.specimenType;
    }

    public void setSpecimenType(SpecimenType specimenType) {
        this.specimenType = specimenType;
    }

    /**
     * The study that this aliquoted specimen belongs to.
     */
    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * If activity status is ACTIVE then this type of specimen has to be
     * collected. If the activity status is closed then this specimen type is no
     * longer being collected for this study.
     */
    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
    }
}
