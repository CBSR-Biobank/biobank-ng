package edu.ualberta.med.biobank.test;

import java.util.Optional;
import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.domain.Patient;

public class TestFixtures {

    public static Patient patientFixture(Factory factory) {
        return patientFixture(factory, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static Patient patientFixture(Factory factory, int numPatients, int numSpecimens, int numAliquots) {
        return patientFixture(factory, Optional.of(numPatients), Optional.of(numPatients), Optional.of(numAliquots));
    }

    public static Patient patientFixture(
        Factory factory,
        Optional<Integer> numCollectionEvents,
        Optional<Integer> numSpecimens,
        Optional<Integer> numAliquots
    ) {
        factory.createAliquotedSpecimen();
        Patient patient = factory.createPatient();

        Comment comment = factory.createComment();
        comment.setMessage(factory.getFaker().lorem().sentence());
        patient.getComments().add(comment);

        var ceCount = numCollectionEvents.orElse(1);
        var spcCount = numSpecimens.orElse(1);
        var alqCount = numAliquots.orElse(1);

        for (int j = 0; j < ceCount; ++j) {
            CollectionEvent cevent = factory.createCollectionEvent();
            comment = factory.createComment();
            comment.setMessage(factory.getFaker().lorem().sentence());
            cevent.getComments().add(comment);

            for (int k = 0; k < spcCount; ++k) {
                factory.createParentSpecimen();

                for (int l = 0; l < alqCount; ++l) {
                    factory.createChildSpecimen();
                }
            }
        }

        return patient;
    }
}
