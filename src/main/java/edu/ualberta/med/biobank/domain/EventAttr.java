package edu.ualberta.med.biobank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "EVENT_ATTR")
public class EventAttr extends DomainEntity {

    @Column(name = "VALUE")
    private String value;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.EventAttr.collectionEvent.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLLECTION_EVENT_ID", nullable = false)
    private CollectionEvent collectionEvent;

    @NotNull(message = "{edu.ualberta.med.biobank.domain.EventAttr.studyEventAttr.NotNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_EVENT_ATTR_ID", nullable = false)
    private StudyEventAttr studyEventAttr;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CollectionEvent getCollectionEvent() {
        return this.collectionEvent;
    }

    public void setCollectionEvent(CollectionEvent collectionEvent) {
        this.collectionEvent = collectionEvent;
    }

    public StudyEventAttr getStudyEventAttr() {
        return this.studyEventAttr;
    }

    public void setStudyEventAttr(StudyEventAttr studyEventAttr) {
        this.studyEventAttr = studyEventAttr;
    }
}
