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
}
