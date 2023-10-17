package edu.ualberta.med.biobank.services;

import edu.ualberta.med.biobank.domain.CollectionEvent;
import edu.ualberta.med.biobank.dtos.CollectionEventDTO;
import edu.ualberta.med.biobank.dtos.CommentDTO;
import edu.ualberta.med.biobank.dtos.EventAttributeDTO;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.permission.patient.CollectionEventReadPermission;
import edu.ualberta.med.biobank.repositories.CollectionEventCustomRepository;
import edu.ualberta.med.biobank.repositories.CollectionEventRepository;
import edu.ualberta.med.biobank.repositories.CollectionEventSpecifications;
import io.jbock.util.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionEventService {

    final Logger logger = LoggerFactory.getLogger(CollectionEventService.class);

    @Autowired
    CollectionEventRepository collectionEventRepository;

    @Autowired
    CollectionEventCustomRepository collectionEventCustomRepository;

    public Either<AppError, CollectionEvent> getByCollectionEventId(Integer id) {
        return collectionEventRepository
            .findById(id)
            .map(Either::<AppError, CollectionEvent>right)
            .orElseGet(() -> Either.left(new EntityNotFound("collectionEvent")));
    }

    public Either<AppError, CollectionEventDTO> findByPnumberAndVnumber(String pnumber, Integer vnumber) {
        var cevents = collectionEventRepository.findAll(
            CollectionEventSpecifications
                .isPatientNumber(pnumber)
                .and(CollectionEventSpecifications.isVisitNumber(vnumber))
        );
        if (cevents.size() <= 0) {
            return Either.left(new EntityNotFound("collection event by pnumber and vnumber"));
        }

        final var cevent = cevents.get(0);
        var permission = new CollectionEventReadPermission(cevent.getPatient().getStudy().getId());
        return permission.isAllowed().map(allowed -> toCollectionEventDTO(cevent));
    }

    public static CollectionEventDTO toCollectionEventDTO(CollectionEvent cevent) {
        return new CollectionEventDTO(
            cevent.getId(),
            cevent.getVisitNumber(),
            cevent.getActivityStatus().getName(),
            cevent.getPatient().getId(),
            cevent.getPatient().getPnumber(),
            cevent.getPatient().getStudy().getId(),
            cevent.getPatient().getStudy().getNameShort(),
            cevent
                .getEventAttrs()
                .stream()
                .map(eventAttr ->
                    new EventAttributeDTO(
                        eventAttr.getStudyEventAttr().getGlobalEventAttr().getLabel(),
                        eventAttr.getValue()
                    )
                )
                .toList(),
            cevent
                .getComments()
                .stream()
                .map(comment ->
                    new CommentDTO(
                        comment.getId(),
                        comment.getMessage(),
                        comment.getUser().getLogin(),
                        comment.getCreatedAt()
                    )
                )
                .toList()
        );
    }
}
