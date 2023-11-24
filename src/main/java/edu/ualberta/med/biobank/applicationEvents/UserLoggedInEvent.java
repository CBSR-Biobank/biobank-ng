package edu.ualberta.med.biobank.applicationEvents;

import org.springframework.context.ApplicationEvent;

public class UserLoggedInEvent extends ApplicationEvent {
  private String name;

  public UserLoggedInEvent(Object source, String name) {
    super(source);
    this.name = name;
    }

    public String getName() {
        return name;
    }
}
