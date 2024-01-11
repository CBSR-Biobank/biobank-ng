package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

/**
 * A storage location
 *
 * ET: The laboratory hosting the storage facilities
 *
 * caTissue Term - Site: A physical location involved in biospecimen collection, storage,
 * processing, or utilization.
 *
 * NCI Term - Repository: A facility where things can be deposited for storage or safekeeping.
 */
@Entity
@DiscriminatorValue("Site")
public class Site extends Center {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SITE_STUDY",
        joinColumns = { @JoinColumn(name = "SITE_ID", nullable = false) },
        inverseJoinColumns = { @JoinColumn(name = "STUDY_ID", nullable = false) })
    private Set<Study> studies = new HashSet<Study>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    private Set<ContainerType> containerTypes = new HashSet<ContainerType>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    private Set<Container> containers = new HashSet<Container>(0);

    public Set<Study> getStudies() {
        return this.studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Set<ContainerType> getContainerTypes() {
        return this.containerTypes;
    }

    public void setContainerTypes(Set<ContainerType> containerTypes) {
        this.containerTypes = containerTypes;
    }

    public Set<Container> getContainers() {
        return this.containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    public Set<Study> getStudiesInternal() {
        return getStudies();
    }
}
