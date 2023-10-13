package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import edu.ualberta.med.biobank.domain.util.RowColPos;

/**
 * A specifically built physical unit that can hold child containers, or can be contained in a
 * parent container.
 *
 */
@Entity
@Table(name = "CONTAINER",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "SITE_ID", "CONTAINER_TYPE_ID", "LABEL" }),
        @UniqueConstraint(columnNames = { "SITE_ID", "PRODUCT_BARCODE" }) })
// TODO: consider pulling @UniqueConstraint into this @Unique annotation,
// because this is a total repeating of constraints. Would then need to figure
// out how to add DDL constraints from our annotations and how to get a bean's
// value of a specific column.
public class Container extends DomainEntity implements HasComments, HasStatus {

    @Column(name = "PRODUCT_BARCODE")
    private String productBarcode;

    @Column(name = "LABEL", nullable = false)
    private String label;

    // TODO: should be decimal?
    @Column(name = "TEMPERATURE")
    private Double temperature;

    @Column(name = "PATH")
    private String path;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "CONTAINER_COMMENT",
        joinColumns = { @JoinColumn(name = "CONTAINER_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false, updatable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentContainer")
    private Set<ContainerPosition> childPositions = new HashSet<ContainerPosition>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOP_CONTAINER_ID")
    private Container topContainer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "container")
    private Set<SpecimenPosition> specimenPositions = new HashSet<SpecimenPosition>(0);

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "container", orphanRemoval = true)
    private ContainerPosition position;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Container.site.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID", nullable = false)
    private Site site;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Container.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status activityStatus = Status.ACTIVE;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Container.containerType.NotNull}")
    @ManyToOne
    @JoinColumn(name = "CONTAINER_TYPE_ID")
    private ContainerType containerType;

    /**
     * Optional.
     *
     * @return
     */
    public String getProductBarcode() {
        return this.productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Set<Comment> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<ContainerPosition> getChildPositions() {
        return this.childPositions;
    }

    public void setChildPositions(Set<ContainerPosition> childPositions) {
        this.childPositions = childPositions;
    }

    public Container getTopContainer() {
        return this.topContainer;
    }

    public void setTopContainer(Container topContainer) {
        this.topContainer = topContainer;
    }

    public Set<SpecimenPosition> getSpecimenPositions() {
        return this.specimenPositions;
    }

    public void setSpecimenPositions(Set<SpecimenPosition> specimenPositions) {
        this.specimenPositions = specimenPositions;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    public ContainerPosition getPosition() {
        return this.position;
    }

    public void setPosition(ContainerPosition position) {
        this.position = position;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
    }

    @Transient
    public RowColPos getPositionAsRowCol() {
        return getPosition() == null ? null : getPosition().getPosition();
    }

    @Transient
    public Container getParentContainer() {
        return getPosition() == null ? null : getPosition()
            .getParentContainer();
    }

    @Transient
    public String getPositionString() {
        Container parent = getParentContainer();
        if (parent != null) {
            RowColPos pos = getPositionAsRowCol();
            if (pos != null) {
                return parent.getContainerType().getPositionString(pos);
            }
        }
        return null;
    }

    public boolean isPositionFree(RowColPos requestedPosition) {
        if (getChildPositions().size() > 0) {
            for (ContainerPosition pos : getChildPositions()) {
                RowColPos rcp = new RowColPos(pos.getRow(), pos.getCol());
                if (requestedPosition.equals(rcp)) {
                    return false;
                }
            }
        }

        // else assume this container has specimens
        for (SpecimenPosition pos : getSpecimenPositions()) {
            RowColPos rcp = new RowColPos(pos.getRow(), pos.getCol());
            if (requestedPosition.equals(rcp)) {
                return false;
            }
        }
        return true;
    }

    @Transient
    public Container getChild(RowColPos requestedPosition) throws Exception {
        for (ContainerPosition pos : getChildPositions()) {
            RowColPos rcp = new RowColPos(pos.getRow(), pos.getCol());
            if (requestedPosition.equals(rcp)) {
                return pos.getContainer();
            }
        }
        return null;
    }

    /**
     * Label can start with parent's label as prefix or without.
     *
     * @param label
     * @return
     * @throws Exception
     */
    @Transient
    public Container getChildByLabel(String childLabel) throws Exception {
        // remove parent label from child label
        if (childLabel.startsWith(getLabel())) {
            childLabel = childLabel.substring(getLabel().length());
        }
        RowColPos pos = getPositionFromLabelingScheme(getLabel());
        return getChild(pos);
    }

    /**
     * position is 2 letters, or 2 number or 1 letter and 1 number... this position string is used
     * to get the correct row and column index the given position String.
     *
     * @throws Exception
     */
    @SuppressWarnings("nls")
    @Transient
    public RowColPos getPositionFromLabelingScheme(String position)
        throws Exception {
        ContainerType containerType = getContainerType();

        if (containerType == null)
            throw new IllegalStateException("container type cannot be null");

        RowColPos rcp = containerType.getRowColFromPositionString(position);
        if (rcp != null) {
            if (rcp.getRow() >= containerType.getRowCapacity()
                || rcp.getCol() >= containerType.getColCapacity()) {
                throw new IllegalArgumentException("position " + position
                    + " (" + rcp + ") is out of bounds of "
                    + containerType.getCapacity());
            }
        }
        return rcp;
    }

    public boolean hasSpecimens() {
        return (getSpecimenPositions().size() > 0);
    }
}
