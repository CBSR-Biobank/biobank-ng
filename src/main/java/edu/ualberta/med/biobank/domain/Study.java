package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Research conducted on a specific group of people to discover a determined
 * result; has one specific protocol
 *
 */
@Entity
@Table(name = "STUDY")
public class Study extends DomainEntity implements HasName, HasNameShort, HasStatus {

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Study.name.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.Study.name.NotBlank}")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @NotEmpty(message = "{edu.ualberta.med.biobank.domain.Study.nameShort.NotEmpty}")
    @NotBlank(message = "{edu.ualberta.med.biobank.domain.Study.nameShort.NotBlank}")
    @Column(name = "NAME_SHORT", unique = true, nullable = false, length = 50)
    private String nameShort;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.Study.activityStatus.NotEmpty}")
    @Column(name = "ACTIVITY_STATUS_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status activityStatus = Status.ACTIVE;

    @OneToMany(mappedBy = "study")
    private Set<Patient> patients = new HashSet<Patient>(0);

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "study")
    private Set<StudyEventAttr> studyEventAttrs = new HashSet<StudyEventAttr>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "STUDY_CONTACT", joinColumns = {
            @JoinColumn(name = "STUDY_ID", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "CONTACT_ID", nullable = false) })
    private Set<Contact> contacts = new HashSet<Contact>(0);

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "study")
    private ResearchGroup researchGroup;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "STUDY_COMMENT", joinColumns = {
            @JoinColumn(name = "STUDY_ID", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "COMMENT_ID", unique = true, nullable = false) })
    private Set<Comment> comments = new HashSet<Comment>(0);

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "study")
    private Set<SourceSpecimen> sourceSpecimens = new HashSet<SourceSpecimen>(0);

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "study")
    private Set<AliquotedSpecimen> aliquotedSpecimens = new HashSet<AliquotedSpecimen>(0);

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

    @Override
    public Status getActivityStatus() {
        return this.activityStatus;
    }

    @Override
    public void setActivityStatus(Status activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public ResearchGroup getResearchGroup() {
        return this.researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public Set<StudyEventAttr> getStudyEventAttrs() {
        return this.studyEventAttrs;
    }

    public void setStudyEventAttrs(Set<StudyEventAttr> studyEventAttrs) {
        this.studyEventAttrs = studyEventAttrs;
    }

    public Set<SourceSpecimen> getSourceSpecimens() {
        return this.sourceSpecimens;
    }

    public void setSourceSpecimens(Set<SourceSpecimen> sourceSpecimens) {
        this.sourceSpecimens = sourceSpecimens;
    }

    public Set<AliquotedSpecimen> getAliquotedSpecimens() {
        return this.aliquotedSpecimens;
    }

    public void setAliquotedSpecimens(Set<AliquotedSpecimen> aliquotedSpecimens) {
        this.aliquotedSpecimens = aliquotedSpecimens;
    }
}
