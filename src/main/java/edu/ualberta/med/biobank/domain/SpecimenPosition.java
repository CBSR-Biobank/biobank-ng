package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SPECIMEN_POSITION",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "CONTAINER_ID", "ROW", "COL" }) })
public class SpecimenPosition extends AbstractPosition {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.SpecimenPosition.container.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTAINER_ID", nullable = false)
    private Container container;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.SpecimenPosition.specimen.NotNull}")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIMEN_ID", nullable = false, unique = true)
    private Specimen specimen;

    @NotNull
    @Column(name = "POSITION_STRING", length = 255, nullable = false)
    private String positionString;

    public Container getContainer() {
        return this.container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Read-only property (the corresponding setter does nothing) to get data
     * for a foreign key constraint to the container, ensuring that as long as
     * this {@link SpecimenPosition} exists, the {@link Container} has the same
     * {@link ContainerType}.
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "CONTAINER_TYPE_ID", nullable = false)
    ContainerType getContainerType() {
        return getContainer() != null
            ? getContainer().getContainerType()
            : null;
    }

    @SuppressWarnings("unused")
    void setContainerType(ContainerType containerType) {
    }

    public Specimen getSpecimen() {
        return this.specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public String getPositionString() {
        return this.positionString;
    }

    public void setPositionString(String positionString) {
        this.positionString = positionString;
    }

    /**
     * Read-only property (the corresponding setter does nothing) to get data
     * for a foreign key constraint to the container, ensuring that as long as
     * this {@link SpecimenPosition} exists, the {@link Specimen} has the same
     * {@link SpecimenType}.
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "SPECIMEN_TYPE_ID", nullable = false)
    SpecimenType getSpecimenType() {
        return getSpecimen() != null
            ? getSpecimen().getSpecimenType()
            : null;
    }

    @SuppressWarnings("unused")
    void setSpecimenType(SpecimenType specimenType) {
    }

    @Override
    @Transient
    public Container getHoldingContainer() {
        return getContainer();
    }
}
