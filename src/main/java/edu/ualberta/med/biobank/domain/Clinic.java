package edu.ualberta.med.biobank.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

/**
 * A collection site that collects biospecimens and transports them to a repository site.
 * Biospecimens are collected from patients that are participating in a study.
 *
 * NCI Term: Collecting laboratory. The laboratory that collects specimens from a study subject.
 */
@Entity
@DiscriminatorValue("Clinic")
public class Clinic extends Center {

    @Column(name = "SENDS_SHIPMENTS")
    private boolean sendsShipments = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clinic")
    private Set<Contact> contacts = new HashSet<Contact>(0);

    // TODO: rename to isSendsShipments
    public boolean getSendsShipments() {
        return this.sendsShipments;
    }

    public void setSendsShipments(boolean sendsShipments) {
        this.sendsShipments = sendsShipments;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    public Set<Study> getStudiesInternal() {
        // the same study could be associated with one or more contacts
        Set<Study> studies = new HashSet<Study>(0);
        for (Contact contact : getContacts()) {
            studies.addAll(contact.getStudies());
        }
        return studies;
    }
}
