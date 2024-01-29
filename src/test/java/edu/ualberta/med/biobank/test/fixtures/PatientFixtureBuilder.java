package edu.ualberta.med.biobank.test.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.test.Factory;

public final class PatientFixtureBuilder {

    private int numCollectionEvents;
    private int numSpecimens;
    private int numAliquots;
    private int numComments;
    private User commentUser;

    public PatientFixtureBuilder() {
        this.numCollectionEvents = 0;
        this.numSpecimens = 0;
        this.numAliquots = 0;
        this.numComments = 0;
        this.commentUser = null;
    }

    public PatientFixtureBuilder numCollectionEvents(int num) {
        this.numCollectionEvents = num;
        return this;
    }

    public PatientFixtureBuilder numSpecimens(int num) {
        if (numCollectionEvents <= 0) {
            this.numCollectionEvents = 1;
        }
        this.numSpecimens = num;
        return this;
    }

    public PatientFixtureBuilder numAliquots(int num) {
        if (numSpecimens <= 0) {
            this.numSpecimens = 1;
        }
        this.numAliquots = num;
        return this;
    }

    public PatientFixtureBuilder numComments(int num) {
        this.numComments = num;
        return this;
    }

    public PatientFixtureBuilder commentUsername(User user) {
        this.commentUser = user;
        return this;
    }

    public Patient build(Factory factory) {
        Patient patient = factory.createPatient();

        for (int j = 0; j < numComments; ++j) {
            Comment comment = factory.createComment();
            comment.setMessage(factory.getFaker().lorem().sentence());
            if (commentUser != null) {
                comment.setUser(commentUser);
            }
            patient.getComments().add(comment);
        }

        for (int j = 0; j < numCollectionEvents; ++j) {
            CollectionEvent cevent = factory.createCollectionEvent();
            Comment comment = factory.createComment();
            comment.setMessage(factory.getFaker().lorem().sentence());
            cevent.getComments().add(comment);

            for (int k = 0; k < numSpecimens; ++k) {
                factory.createParentSpecimen();

                for (int l = 0; l < numAliquots; ++l) {
                    factory.createChildSpecimen();
                }
            }
        }

        return patient;
    }
}
