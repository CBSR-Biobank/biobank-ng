package edu.ualberta.med.biobank.test.fixtures;

import edu.ualberta.med.biobank.domain.EventAttrType;
import edu.ualberta.med.biobank.domain.GlobalEventAttr;
import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.domain.Study;
import edu.ualberta.med.biobank.domain.StudyEventAttr;
import edu.ualberta.med.biobank.dtos.AnnotationTypeDTO;
import edu.ualberta.med.biobank.test.Factory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public final class StudyFixtureBuilder {

    private List<StudyEventAttr> attributeTypes;

    private EntityManager em;

    public StudyFixtureBuilder() {
        this.attributeTypes = new ArrayList<>();
        this.em = null;
    }

    public StudyFixtureBuilder setEntityManger(EntityManager em) {
        this.em = em;
        return this;
    }

    public StudyFixtureBuilder withAttributeType(
        String type,
        String label,
        String status,
        boolean required,
        String... validValues
    ) {
        if (em == null) {
            throw new RuntimeException("entity manager not set");
        }

        EventAttrType attrType;
        switch (type) {
            case "number" -> {
                attrType = em.getReference(EventAttrType.class, 1);
            }
            case "text" -> {
                attrType = em.getReference(EventAttrType.class, 2);
            }
            case "date_time" -> {
                attrType = em.getReference(EventAttrType.class, 3);
            }
            case "select_single" -> {
                attrType = em.getReference(EventAttrType.class, 4);
            }
            case "select_multiple" -> {
                attrType = em.getReference(EventAttrType.class, 5);
            }
            default -> {
                attrType = new EventAttrType();
                attrType.setName(type);
            }
        }

        GlobalEventAttr base = new GlobalEventAttr();
        base.setEventAttrType(attrType);
        base.setLabel(label);

        StudyEventAttr attr = new StudyEventAttr();
        attr.setGlobalEventAttr(base);
        attr.setActivityStatus(Status.fromName(status));
        if (validValues != null) {
            attr.setPermissible(String.join(";", validValues));
        }
        attr.setRequired(required);

        attributeTypes.add(attr);
        return this;
    }

    public StudyFixtureBuilder withAttributeType(
        String type,
        String label,
        String status,
        boolean required
    ) {
        return withAttributeType(type, label, status, required, (String[]) null);
    }

    public StudyFixtureBuilder withAttributeType(AnnotationTypeDTO dto) {
        return withAttributeType(dto.type(), dto.label(), dto.status(), dto.required(), dto.validValues());
    }

    public Study build(Factory factory) {
        if (em == null) {
            throw new RuntimeException("entity manager not set");
        }

        Study study = factory.createStudy();
        attributeTypes
            .stream()
            .forEach(attrType -> {
                attrType.setStudy(study);
                em.persist(attrType.getGlobalEventAttr().getEventAttrType());
                em.persist(attrType.getGlobalEventAttr());
                em.persist(attrType);
            });
        study.getStudyEventAttrs().addAll(attributeTypes);
        em.flush();
        return study;
    }
}
