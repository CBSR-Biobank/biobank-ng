package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.Type;

import edu.ualberta.med.biobank.domain.type.LabelingLayout;
import edu.ualberta.med.biobank.domain.util.RowColPos;

/**
 * Describes a container configuration which may hold other child containers or
 * specimens. Container
 * types are used to create a representation of a physical container
 *
 * ET: Describes various containers that can hold specimens, these container
 * types are used to build
 * a container.
 *
 */
@Entity
@Table(name = "CONTAINER_TYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "SITE_ID", "NAME" }),
        @UniqueConstraint(columnNames = { "SITE_ID", "NAME_SHORT" }) })
public class ContainerType extends DomainEntity implements HasName, HasNameShort, HasStatus, HasComments {

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_SHORT")
    private String nameShort;

    @Column(name = "TOP_LEVEL")
    private boolean topLevel = false;

    @Column(name = "IS_MICROPLATE")
    private boolean isMicroplate = false;

    // TODO: change to decimal
    @Column(name = "DEFAULT_TEMPERATURE")
    private Double defaultTemperature;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONTAINER_TYPE_SPECIMEN_TYPE", joinColumns = {
            @JoinColumn(name = "CONTAINER_TYPE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "SPECIMEN_TYPE_ID", nullable = false, updatable = false) })
    private Set<SpecimenType> specimenTypes = new HashSet<SpecimenType>(0);

    @SQLInsert(sql = "INSERT INTO `CONTAINER_TYPE_CONTAINER_TYPE` (PARENT_CONTAINER_TYPE_ID, CHILD_CONTAINER_TYPE_ID, SITE_ID) SELECT ?, ID, SITE_ID FROM `CONTAINER_TYPE` WHERE ID = ?")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONTAINER_TYPE_CONTAINER_TYPE", joinColumns = {
            @JoinColumn(name = "PARENT_CONTAINER_TYPE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "CHILD_CONTAINER_TYPE_ID", nullable = false, updatable = false) })
    private Set<ContainerType> childContainerTypes = new HashSet<ContainerType>(0);

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerType.activityStatus.NotNull}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status activityStatus = Status.ACTIVE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CONTAINER_TYPE_COMMENT", joinColumns = {
            @JoinColumn(name = "CONTAINER_TYPE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false, updatable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @Valid
    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerType.capacity.NotNull}")
    @Embedded
    private Capacity capacity = new Capacity();

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerType.site.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID", nullable = false)
    private Site site;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.ContainerType.childLabelingScheme.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHILD_LABELING_SCHEME_ID", nullable = false)
    private ContainerLabelingScheme childLabelingScheme;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "childContainerTypes")
    private Set<ContainerType> parentContainerTypes = new HashSet<ContainerType>(0);

    @Column(name = "LABELING_LAYOUT")
    @Enumerated(EnumType.ORDINAL)
    private LabelingLayout labelingLayout;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getNameShort() {
        return this.nameShort;
    }

    @Override
    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    // TODO: rename to isTopLevel
    public boolean getTopLevel() {
        return this.topLevel;
    }

    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }

    public boolean getIsMicroplate() {
        return this.isMicroplate;
    }

    public void setIsMicroplate(boolean isMicroplate) {
        this.isMicroplate = isMicroplate;
    }

    public Double getDefaultTemperature() {
        return this.defaultTemperature;
    }

    public void setDefaultTemperature(Double defaultTemperature) {
        this.defaultTemperature = defaultTemperature;
    }

    public Set<SpecimenType> getSpecimenTypes() {
        return this.specimenTypes;
    }

    public void setSpecimenTypes(Set<SpecimenType> specimenTypes) {
        this.specimenTypes = specimenTypes;
    }

    /**
     * The custom @SQLInsert allows a `SITE_ID` to be inserted into the correlation
     * table so a
     * foreign key can be created to ensure that {@link ContainerType}-s with the
     * same {@link Site}
     * can be related.
     *
     * @return
     */
    public Set<ContainerType> getChildContainerTypes() {
        return this.childContainerTypes;
    }

    public void setChildContainerTypes(Set<ContainerType> childContainerTypes) {
        this.childContainerTypes = childContainerTypes;
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

    public Capacity getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ContainerLabelingScheme getChildLabelingScheme() {
        return this.childLabelingScheme;
    }

    public void setChildLabelingScheme(ContainerLabelingScheme childLabelingScheme) {
        this.childLabelingScheme = childLabelingScheme;
    }

    public Set<ContainerType> getParentContainerTypes() {
        return this.parentContainerTypes;
    }

    public void setParentContainerTypes(Set<ContainerType> parentContainerTypes) {
        this.parentContainerTypes = parentContainerTypes;
    }

    @Transient
    public Integer getRowCapacity() {
        return this.getCapacity().getRowCapacity();
    }

    @Transient
    public Integer getColCapacity() {
        return this.getCapacity().getColCapacity();
    }

    @Transient
    public String getPositionString(RowColPos position) {
        return ContainerLabelingScheme.getPositionString(
                position,
                getChildLabelingScheme().getId(),
                getRowCapacity(),
                getColCapacity(),
                labelingLayout);

    }

    @Transient
    public RowColPos getRowColFromPositionString(String position) throws Exception {
        return getChildLabelingScheme().getRowColFromPositionString(
                position, getRowCapacity(), getColCapacity(), getLabelingLayout());
    }

    public LabelingLayout getLabelingLayout() {
        return this.labelingLayout;
    }

    public void setLabelingLayout(LabelingLayout labelingLayout) {
        this.labelingLayout = labelingLayout;
    }

    @Transient
    public boolean hasMultipleLabelingLayout() {
        Integer rows = getRowCapacity();
        Integer cols = getColCapacity();
        if ((rows == null) || (cols == null) || (childLabelingScheme == null)) {
            return false;
        }
        return this.childLabelingScheme.getHasMultipleLayout()
                && (getRowCapacity() > 1) && (getColCapacity() > 1);
    }
}
