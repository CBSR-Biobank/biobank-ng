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
import jakarta.persistence.Transient;

@Entity
@Table(name = "DOMAIN")
public class Domain extends DomainEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOMAIN_CENTER", joinColumns = {
            @JoinColumn(name = "DOMAIN_ID", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "CENTER_ID", nullable = false) })
    private Set<Center> centers = new HashSet<Center>(0);

    // FIXME: column should be named "STUDY_ID" and not "CENTER_ID"
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOMAIN_STUDY", joinColumns = @JoinColumn(name = "DOMAIN_ID", nullable = false), inverseJoinColumns = @JoinColumn(name = "CENTER_ID", nullable = false))
    private Set<Study> studies = new HashSet<Study>(0);

    @Column(name = "ALL_CENTERS")
    private boolean allCenters = false;

    @Column(name = "ALL_STUDIES")
    private boolean allStudies = false;

    public Domain() {
    }

    public Domain(Domain domain) {
        getCenters().addAll(domain.getCenters());
        getStudies().addAll(domain.getStudies());

        setAllCenters(domain.isAllCenters());
        setAllStudies(domain.isAllStudies());
    }

    public Set<Center> getCenters() {
        return centers;
    }

    public void setCenters(Set<Center> centers) {
        this.centers = centers;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public boolean isAllCenters() {
        return allCenters;
    }

    public void setAllCenters(boolean allCenters) {
        this.allCenters = allCenters;
        if (allCenters)
            getCenters().clear();
    }

    public boolean isAllStudies() {
        return allStudies;
    }

    public void setAllStudies(boolean allStudies) {
        this.allStudies = allStudies;
        if (allStudies)
            getStudies().clear();
    }

    @Transient
    public boolean isGlobal() {
        return isAllCenters() && isAllStudies();
    }

    @Transient
    public boolean isSuperset(Domain that) {
        boolean allCenters = containsAllCenters(that);
        boolean allStudies = containsAllStudies(that);
        return allCenters && allStudies;
    }

    @Transient
    public boolean contains(Center center) {
        return isAllCenters() || getCenters().contains(center);
    }

    /**
     * Done on a {@link Domain} instead of a set of {@link Center}-s because if
     * the given {@link Domain} returns true for {@link #isAllCenters()} but has
     * an empty set from {@link #getCenters()}, then that is very misleading.
     *
     * @param domain
     * @return
     */
    @Transient
    public boolean containsAllCenters(Domain that) {
        return isAllCenters()
                || (!that.isAllCenters() && getCenters()
                        .containsAll(that.getCenters()));
    }

    @Transient
    public boolean contains(Study study) {
        return isAllStudies() || getStudies().contains(study);
    }

    @Transient
    public boolean containsAllStudies(Domain that) {
        return isAllStudies()
                || (!that.isAllStudies() && getStudies()
                        .containsAll(that.getStudies()));
    }

    @Transient
    public boolean isEquivalent(Domain that) {
        boolean equivalent = true;
        equivalent &= isAllCenters() == that.isAllCenters();
        equivalent &= isAllStudies() == that.isAllStudies();
        equivalent &= getCenters().equals(that.getCenters());
        equivalent &= getStudies().equals(that.getStudies());
        return equivalent;
    }
}
