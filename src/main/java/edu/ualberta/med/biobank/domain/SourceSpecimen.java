package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * caTissue Term - Specimen: A single unit of tissue or body fluid collected
 * from a participant as part of a specimen collection event. A new specimen can
 * be created as a derivative of an existing specimen or by dividing it into
 * small pieces.
 *
 * NCI Term - Biospecimen: Any material sample taken from a biological entity
 * for testing, diagnostic, propagation, treatment or research purposes,
 * including a sample obtained from a living organism or taken from the
 * biological object after halting of all its life functions. Biospecimen can
 * contain one or more components including but not limited to cellular
 * molecules, cells, tissues, organs, body fluids, embryos, and body excretory
 * products.
 *
 */
@Entity
@Table(name = "SOURCE_SPECIMEN")
public class SourceSpecimen extends DomainEntity {

    @Column(name = "NEED_ORIGINAL_VOLUME")
    private boolean needOriginalVolume = false;

    @NotNull(message = "{edu.ualberta.med.biobank.model.SourceSpecimen.specimenType.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_TYPE_ID", nullable = false)
    private SpecimenType specimenType;

    @NotNull(message = "{edu.ualberta.med.biobank.model.SourceSpecimen.study.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID", nullable = false)
    private Study study;

    // TODO: rename to isNeedOriginalVolume
    public boolean getNeedOriginalVolume() {
        return this.needOriginalVolume;
    }

    public void setNeedOriginalVolume(boolean needOriginalVolume) {
        this.needOriginalVolume = needOriginalVolume;
    }

    public SpecimenType getSpecimenType() {
        return this.specimenType;
    }

    public void setSpecimenType(SpecimenType specimenType) {
        this.specimenType = specimenType;
    }

    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
