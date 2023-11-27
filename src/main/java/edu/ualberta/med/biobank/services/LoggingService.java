package edu.ualberta.med.biobank.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import edu.ualberta.med.biobank.applicationEvents.PatientReadEvent;
import edu.ualberta.med.biobank.applicationEvents.UserLoggedInEvent;
import edu.ualberta.med.biobank.domain.Log;
import edu.ualberta.med.biobank.repositories.LoggingRepository;

@Service
public class LoggingService {

    final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    private LoggingRepository loggingRepository;

    public LoggingService(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    @EventListener
    void handleEmployeeEvent(UserLoggedInEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("login")
            .username(event.getUsername())
            .build();
        loggingRepository.save(logEvent);
    }

    @EventListener
    void handlePatientReadEvent(PatientReadEvent event) {
        Log logEvent = new Log.LogBuilder()
            .action("select")
            .username(event.getUsername())
            .patientNumber(event.getPnumber())
            .type("Patient")
            .build();
        loggingRepository.save(logEvent);
    }
}
