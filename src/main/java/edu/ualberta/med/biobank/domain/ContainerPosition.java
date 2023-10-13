package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "CONTAINER_POSITION",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "PARENT_CONTAINER_ID", "ROW", "COL" }) })
public class ContainerPosition extends AbstractPosition {

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerPosition.parentContainer.NotNull}")
    @ManyToOne
    @JoinColumn(name = "PARENT_CONTAINER_ID")
    private Container parentContainer;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerPosition.container.NotNull}")
    @OneToOne
    @JoinColumn(name = "CONTAINER_ID")
    private Container container;

    public Container getParentContainer() {
        return parentContainer;
    }

    public void setParentContainer(Container parentContainer) {
        this.parentContainer = parentContainer;
    }

    /**
     * Read-only property (the corresponding setter does nothing) to get data
     * for a foreign key constraint to the parent container, ensuring that as
     * long as this {@link ContainerPosition} exists, the parent
     * {@link Container} has the same {@link ContainerType}.
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_CONTAINER_TYPE_ID", nullable = false)
    ContainerType getParentContainerType() {
        return getParentContainer() != null
            ? getParentContainer().getContainerType()
            : null;
    }

    @SuppressWarnings("unused")
    void setParentContainerType(ContainerType parentContainerType) {
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Read-only property (the corresponding setter does nothing) to get data
     * for a foreign key constraint to the container, ensuring that as long as
     * this {@link ContainerPosition} exists, parent {@link Container} has the
     * same {@link ContainerType}.
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

    @Override
    @Transient
    public Container getHoldingContainer() {
        return getParentContainer();
    }
}
