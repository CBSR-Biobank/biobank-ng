package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("ResearchGroup")
public class ResearchGroup extends Center {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_ID", unique = true)
    private Study study;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESEARCH_GROUP_ID", updatable = false)
    private Set<Request> requests = new HashSet<Request>(0);

    public Study getStudy() {
        return this.study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Set<Request> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }
}
