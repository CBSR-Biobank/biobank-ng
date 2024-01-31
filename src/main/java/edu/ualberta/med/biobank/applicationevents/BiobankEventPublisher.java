package edu.ualberta.med.biobank.applicationevents;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BiobankEventPublisher {
    private ApplicationEventPublisher applicationEventPublisher;

    public BiobankEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishUserLoggedIn(final String username) {
        UserLoggedInEvent event = new UserLoggedInEvent(this, username);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishPatientRead(final String username, final String pnumber) {
        PatientReadEvent event = new PatientReadEvent(this, username, pnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishPatientCreated(final String username, final String pnumber) {
        PatientCreatedEvent event = new PatientCreatedEvent(this, username, pnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishPatientUpdated(final String username, final String pnumber) {
        PatientUpdatedEvent event = new PatientUpdatedEvent(this, username, pnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishVisitRead(final String username, final String pnumber, final Integer vnumber) {
        var event = new VisitReadEvent(this, username, pnumber, vnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishVisitCreated(String username, String pnumber, Integer vnumber) {
        var event = new VisitCreatedEvent(this, username, pnumber, vnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishVisitUpdated(String username, String pnumber, Integer vnumber) {
        var event = new VisitUpdatedEvent(this, username, pnumber, vnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishVisitDeleted(String username, String pnumber, Integer vnumber) {
        var event = new VisitDeletedEvent(this, username, pnumber, vnumber);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishSpecimenRead(final String username, final String pnumber, final String inventoryId) {
        var event = new SpecimenReadEvent(this, username, pnumber, inventoryId);
        applicationEventPublisher.publishEvent(event);
    }
}
