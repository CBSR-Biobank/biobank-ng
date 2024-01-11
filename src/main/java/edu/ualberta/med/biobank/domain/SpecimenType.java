package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "SPECIMEN_TYPE")
public class SpecimenType extends DomainEntity
    implements HasName, HasNameShort {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.SpecimenType.name.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.SpecimenType.name.NotBlank}")
    @Column(name = "NAME", unique = true)
    private String name;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.SpecimenType.nameShort.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.SpecimenType.nameShort.NotBlank}")
    @Column(name = "NAME_SHORT", unique = true)
    private String nameShort;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specimenTypes")
    private Set<ContainerType> containerTypes = new HashSet<ContainerType>(0);

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "childSpecimenTypes")
    private Set<SpecimenType> parentSpecimenTypes = new HashSet<SpecimenType>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SPECIMEN_TYPE_SPECIMEN_TYPE",
        joinColumns = { @JoinColumn(name = "PARENT_SPECIMEN_TYPE_ID", nullable = false) },
        inverseJoinColumns = { @JoinColumn(name = "CHILD_SPECIMEN_TYPE_ID", nullable = false) })
    private Set<SpecimenType> childSpecimenTypes = new HashSet<SpecimenType>(0);

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

    public Set<ContainerType> getContainerTypes() {
        return this.containerTypes;
    }

    public void setContainerTypes(Set<ContainerType> containerTypes) {
        this.containerTypes = containerTypes;
    }

    public Set<SpecimenType> getParentSpecimenTypes() {
        return this.parentSpecimenTypes;
    }

    public void setParentSpecimenTypes(Set<SpecimenType> parentSpecimenTypes) {
        this.parentSpecimenTypes = parentSpecimenTypes;
    }

    public Set<SpecimenType> getChildSpecimenTypes() {
        return this.childSpecimenTypes;
    }

    public void setChildSpecimenTypes(Set<SpecimenType> childSpecimenTypes) {
        this.childSpecimenTypes = childSpecimenTypes;
    }
}
