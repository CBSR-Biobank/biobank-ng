package edu.ualberta.med.biobank.test;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Patient;
import edu.ualberta.med.biobank.domain.Specimen;

import java.util.Optional;

public class TestFixtures {

    public static Patient patientFixture(Factory factory) {
        return patientFixture(factory, Optional.empty(), Optional.empty());
    }

    public static Patient patientFixture(
        Factory factory,
        Optional<Integer> numCollectionEvents,
        Optional<Integer> numSpecimens
    ) {
        Patient patient = factory.createPatient();

        Comment comment = factory.createComment();
        comment.setMessage(factory.getFaker().lorem().sentence());
        patient.getComments().add(comment);

        var ceCount = numCollectionEvents.orElse(1);
        var spcCount = numSpecimens.orElse(1);

        for (int j = 0; j < ceCount; ++j) {
            CollectionEvent cevent = factory.createCollectionEvent();
            comment = factory.createComment();
            comment.setMessage(factory.getFaker().lorem().sentence());
            cevent.getComments().add(comment);

            for (int k = 0; k < spcCount; ++k) {
                Specimen specimen = factory.createParentSpecimen();
                comment = factory.createComment();
                comment.setMessage(factory.getFaker().lorem().sentence());
                specimen.getComments().add(comment);
            }
        }

        return patient;
    }
}
