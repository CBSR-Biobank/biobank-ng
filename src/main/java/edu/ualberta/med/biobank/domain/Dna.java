package edu.ualberta.med.biobank.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "DNA")
public class Dna extends DomainEntity {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Dna.specimen.NotNull}")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIMEN_ID", nullable = false, unique = true)
    private Specimen specimen;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.concentrationAbs.Digits}")
    @Column(name = "CONCENTRATION_ABS", precision = 10, scale = 10)
    private BigDecimal concentrationAbs;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.concentrationFluor.Digits}")
    @Column(name = "CONCENTRATION_FLUOR", precision = 10, scale = 10)
    private BigDecimal concentrationFluor;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.od260Over280.Digits}")
    @Column(name = "OD_260_OVER_280", precision = 10, scale = 10)
    private BigDecimal od260Over280;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.od260Over230.Digits}")
    @Column(name = "OD_260_OVER_230", precision = 10, scale = 10)
    private BigDecimal od260Over230;

    @Digits(integer = 10, fraction = 10, message = "{edu.ualberta.med.biobank.domain.Specimen.aliquotYield.Digits}")
    @Column(name = "ALIQUOT_YIELD", precision = 10, scale = 10)
    private BigDecimal aliquotYield;

    public Specimen getSpecimen() {
        return this.specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public BigDecimal getConcentrationAbs() {
        return this.concentrationAbs;
    }

    public void setConcentrationAbs(BigDecimal concentrationAbs) {
        this.concentrationAbs = concentrationAbs;
    }

    public BigDecimal getConcentrationFluor() {
        return this.concentrationFluor;
    }

    public void setConcentrationFluor(BigDecimal concentrationFluor) {
        this.concentrationFluor = concentrationFluor;
    }

    public BigDecimal getOd260Over280() {
        return this.od260Over280;
    }

    public void setOd260Over280(BigDecimal od260Over280) {
        this.od260Over280 = od260Over280;
    }

    public BigDecimal getOd260Over230() {
        return this.od260Over230;
    }

    public void setOd260Over230(BigDecimal od260Over230) {
        this.od260Over230 = od260Over230;
    }

    public BigDecimal getAliquotYield() {
        return this.aliquotYield;
    }

    public void setAliquotYield(BigDecimal aliquotYield) {
        this.aliquotYield = aliquotYield;
    }
}
