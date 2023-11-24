package edu.ualberta.med.biobank.applicationEvents;

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
}
