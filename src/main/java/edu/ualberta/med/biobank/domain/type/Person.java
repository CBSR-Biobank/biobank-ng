package edu.ualberta.med.biobank.domain.type;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

import edu.ualberta.med.biobank.domain.User;

@Embeddable
public class Person {

    @Length(max = 63, message = "{Person.name.Length}")
    @Column(name = "PERSON_NAME", length = 63)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_USER_ID")
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
