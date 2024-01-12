package edu.ualberta.med.biobank.matchers;

import edu.ualberta.med.biobank.domain.Specimen;
import edu.ualberta.med.biobank.dtos.AliquotSpecimenDTO;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliquotMatcher extends TypeSafeDiagnosingMatcher<AliquotSpecimenDTO> {

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(AliquotMatcher.class);

    private final Specimen specimen;

    public AliquotMatcher(final Specimen specimen) {
        this.specimen = specimen;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("matches specimen with inventory ID=`" + specimen.getInventoryId() + "`");
    }

    @Override
    public boolean matchesSafely(final AliquotSpecimenDTO dto, Description mismatchDescription) {
        List<String> errors = new ArrayList<>();
        if (dto.id() != specimen.getId()) {
            errors.add("IDs don't match: %d".formatted(dto.id()));
        }

        if (!dto.inventoryId().matches(specimen.getInventoryId())) {
            errors.add("inventory IDs don't match: %s".formatted(dto.inventoryId()));
        }

        if (specimen.getCreatedAt().toInstant().until(dto.createdAt().toInstant(), ChronoUnit.SECONDS) > 0) {
            errors.add("created at difference more than a second: %s".formatted(dto.createdAt()));
        }

        if (specimen.getParentSpecimen() != null) {
            var parent = specimen.getParentSpecimen();
            if (dto.parentId() != parent.getId()) {
                errors.add("parent IDs don't match: %s".formatted(dto.parentId()));
            }

            if (dto.processingEventId() != parent.getProcessingEvent().getId()) {
                errors.add("processing event IDs don't match: %s".formatted(dto.processingEventId()));
            }

            if (!dto.worksheet().equals(parent.getProcessingEvent().getWorksheet())) {
                errors.add("worksheets don't match: %s".formatted(dto.worksheet()));
            }
        } else {
            if (dto.parentId() != null) {
                errors.add("parent IDs don't match: %s".formatted(dto.parentId()));
            }

            if (dto.processingEventId() != null) {
                errors.add("processing event IDs don't match: %s".formatted(dto.processingEventId()));
            }

            if (!dto.worksheet().isBlank()) {
                errors.add("worksheets don't match: %s".formatted(dto.worksheet()));
            }
        }

        if (specimen.getQuantity() != null) {
            if (dto.quantity().compareTo(specimen.getQuantity()) != 0) {
                errors.add("quantities don't match: %s".formatted(dto.quantity()));
            }
        }

        if (!dto.status().equals(specimen.getActivityStatus().toString())) {
            errors.add("status don't match: %s".formatted(dto.status()));
        }

        if (dto.specimenTypeId() != specimen.getSpecimenType().getId()) {
            errors.add("specimen type IDs don't match: %s".formatted(dto.specimenTypeId()));
        }

        if (!dto.specimenTypeNameShort().equals(specimen.getSpecimenType().getNameShort())) {
            errors.add("status don't match: %s".formatted(dto.specimenTypeNameShort()));
        }

        if (dto.originCenterId() != specimen.getOriginInfo().getCenter().getId()) {
            errors.add("origin center IDs don't match: %s".formatted(dto.originCenterId()));
        }

        if (!dto.originCenterNameShort().equals(specimen.getOriginInfo().getCenter().getNameShort())) {
            errors.add("origin center names don't match: %s".formatted(dto.originCenterNameShort()));
        }

        if (dto.currentCenterId() != specimen.getCurrentCenter().getId()) {
            errors.add("current center IDs don't match: %s".formatted(dto.currentCenterId()));
        }

        if (!dto.currentCenterNameShort().equals(specimen.getCurrentCenter().getNameShort())) {
            errors.add("current center names don't match: %s".formatted(dto.currentCenterNameShort()));
        }

        if (dto.hasComments().booleanValue() == specimen.getComments().isEmpty()) {
            errors.add("has comments don't match: %s".formatted(dto.hasComments()));
        }

        if (!dto.patientNumber().equals(specimen.getCollectionEvent().getPatient().getPnumber())) {
            errors.add("patient numbers don't match: %s".formatted(dto.patientNumber()));
        }

        if (dto.studyId() != specimen.getCollectionEvent().getPatient().getStudy().getId()) {
            errors.add("study IDs don't match: %s".formatted(dto.studyId()));
        }

        if (!dto.studyNameShort().equals(specimen.getCollectionEvent().getPatient().getStudy().getNameShort())) {
            errors.add("study IDs don't match: %s".formatted(dto.studyNameShort()));
        }

        mismatchDescription.appendText(errors.stream().collect(Collectors.joining(" and ")));
        return errors.isEmpty();
    }

    public static AliquotMatcher matchesAliquot(final Specimen specimen) {
        return new AliquotMatcher(specimen);
    }
}
