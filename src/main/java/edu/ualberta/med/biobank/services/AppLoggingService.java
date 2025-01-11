package edu.ualberta.med.biobank.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationevents.PatientCreatedEvent;
import edu.ualberta.med.biobank.applicationevents.PatientReadEvent;
import edu.ualberta.med.biobank.applicationevents.PatientUpdatedEvent;
import edu.ualberta.med.biobank.applicationevents.SpecimenPullRequestEvent;
import edu.ualberta.med.biobank.applicationevents.SpecimenReadEvent;
import edu.ualberta.med.biobank.applicationevents.StudyCatalogueDownloadEvent;
import edu.ualberta.med.biobank.applicationevents.StudyCatalogueRequestEvent;
import edu.ualberta.med.biobank.applicationevents.UserLoggedInEvent;
import edu.ualberta.med.biobank.applicationevents.VisitCreatedEvent;
import edu.ualberta.med.biobank.applicationevents.VisitDeletedEvent;
import edu.ualberta.med.biobank.applicationevents.VisitReadEvent;
import edu.ualberta.med.biobank.applicationevents.VisitUpdatedEvent;
import edu.ualberta.med.biobank.domain.Log;
import edu.ualberta.med.biobank.dtos.AppLogEntryDTO;
import edu.ualberta.med.biobank.repositories.AppLoggingRepository;
import edu.ualberta.med.biobank.repositories.CustomAppLoggingRepository;

@Service
public class AppLoggingService {

    @SuppressWarnings("unused")
    final Logger logger = LoggerFactory.getLogger(AppLoggingService.class);

    private AppLoggingRepository loggingRepository;

    private CustomAppLoggingRepository customLoggingRepository;

    public AppLoggingService(AppLoggingRepository loggingRepository, CustomAppLoggingRepository customAppLoggingRepository) {
        this.loggingRepository = loggingRepository;
        this.customLoggingRepository = customAppLoggingRepository;
    }

    public Page<AppLogEntryDTO> paginated(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable = null;
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "createdAt");
        }
        var result = customLoggingRepository.paginated(pageable);
        return result;
    }

    @EventListener
    void handleEmployeeEvent(UserLoggedInEvent event) {
        logger.info("UserLoggedIn: {}", event.getUsername());
        Log logEvent = new Log.LogBuilder()
            .action("login")
            .username(event.getUsername())
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handlePatientReadEvent(PatientReadEvent event) {
        logger.info("PatientRead: {}", event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("Patient")
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handlePatientCreatedEvent(PatientCreatedEvent event) {
        logger.info("PatientCreated: {}", event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("insert")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("Patient")
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handlePatientUpdatedEvent(PatientUpdatedEvent event) {
        logger.info("PatientUpdated: {}", event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("update")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("Patient")
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitReadEvent(VisitReadEvent event) {
        logger.info("VisitRead: visit {} on patient {}", event.getVnumber(), event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("CollectionEvent")
            .details("visit: %s".formatted(event.getVnumber()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitCreatedEvent(VisitCreatedEvent event) {
        logger.info("VisitCreated: visit {} on patient {}", event.getVnumber(), event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("insert")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("CollectionEvent")
            .details("visit: %s".formatted(event.getVnumber()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitUpdatedEvent(VisitUpdatedEvent event) {
        logger.info("VisitUpdated: visit {} on patient {}", event.getVnumber(), event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("update")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("CollectionEvent")
            .details("visit: %s".formatted(event.getVnumber()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleVisitDeletedEvent(VisitDeletedEvent event) {
        logger.info("VisitDeleted: visit {} on patient {}", event.getVnumber(), event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("delete")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("CollectionEvent")
            .details("visit: %s".formatted(event.getVnumber()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleSpecimenReadEvent(SpecimenReadEvent event) {
        logger.info("SpecimenRead: specimen {} on patient {}", event.getInventoryId(), event.getPnumber());
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .inventoryId(event.getInventoryId())
            .type("Specimen")
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleStudyCatalougeRequestEvent(StudyCatalogueRequestEvent event) {
        logger.info("StudyCatalogueRequestEvent: study {}", event.getStudyNameShort());
        Log logEvent = new Log.LogBuilder()
            .action("request")
            .username(event.getUsername())
            .type("StudyCatalogue")
            .details("study: %s".formatted(event.getStudyNameShort()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleStudyCatalougeDownloadEvent(StudyCatalogueDownloadEvent event) {
        logger.info("StudyCatalogueDownloadEvent: filename {}", event.getFilename());
        Log logEvent = new Log.LogBuilder()
            .action("download")
            .username(event.getUsername())
            .type("StudyCatalogue")
            .details("filename: %s".formatted(event.getFilename()))
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handleSpecimenPullRequestEvent(SpecimenPullRequestEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("S3 - Specimen Request by CSV file")
            .username(event.getUsername())
            .type("report")
            .build();
        loggingRepository.save(logEvent);
    }
}
